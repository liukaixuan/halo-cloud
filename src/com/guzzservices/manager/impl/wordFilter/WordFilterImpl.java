package com.guzzservices.manager.impl.wordFilter;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.guzz.exception.IllegalParameterException;
import org.guzz.util.StringUtil;

import com.guzzservices.business.FilterWord;
import com.guzzservices.business.FilterWordGroup;
import com.guzzservices.manager.Constants;
import com.guzzservices.manager.IFilterWordGroupManager;
import com.guzzservices.manager.IFilterWordManager;
import com.guzzservices.manager.IWordFilter;
import com.guzzservices.rpc.server.CommandHandler;
import com.guzzservices.rpc.server.CommandServerService;
import com.guzzservices.rpc.util.JsonUtil;
import com.guzzservices.secure.wordFilter.MatchResult;
import com.guzzservices.secure.wordFilter.WordFilterRequest;
import com.guzzservices.version.NewVersionListener;
import com.guzzservices.version.VersionControlService;


/**
 * 过滤词引擎.可以实现分组xml字典的过滤处理，支持大小写不敏感，支持过滤词分级 ，支持标记颜色分级.
 */
public class WordFilterImpl implements IWordFilter, CommandHandler, NewVersionListener {
	protected static final Logger LOG = Logger.getLogger(WordFilterImpl.class);
	
	/** 词典 */
	protected Map<String, Dictionary> dicMap = new HashMap<String, Dictionary>();
	
	/**忽略的字符*/
	private boolean[] ingoreChars = new boolean[65535];
	
	/**忽略的字符*/
	private String stopChars = "" ;

	/** 过滤词反显使用标记开始部分。过滤词标红原理：标红内容=〉wordMarkPrefixStartTag + 标红的颜色 + wordMarkPrefixEndTag + 标红内容 + wordMarkSuffix */
	private String wordMarkPrefixStartTag = "<font color='";

	private String wordMarkPrefixEndTag = "'>";
	
	private String wordMarkSuffix = "</font>";

	private IFilterWordManager filterWordManager ;
	
	private IFilterWordGroupManager filterWordGroupManager ;
	
	private VersionControlService versionControlService ;
		
	public WordFilterImpl(){
		for(int i = 0 ; i < ingoreChars.length ; i++){
			ingoreChars[i] = false ;
		}
		
		//过滤时忽略的字符。注意"<>"两个符号不能添加进去！！！否则会引起如：过滤 z<font color=...，而过滤词含有zf，此时'<'被跳过，z<f被当作过滤词，造成html语法被破坏！
		//同时不包括：" '
		//TODO 为了防止不必要的错误，去掉：: [ ] . # 符号.防止html, css, ubb代码错误。
		char[] m_ingoreChars = new char[]{' ','　','!','！', '“','”','‘','’', '《', '》',':', '：',  '(', ')', '[', ']', '【', '】', '☆', '★', '{', '}', '（', '）', '！', '、', ';', ',', '.','@', '&', '，','；','。','*','·','…','-', '_', '+','×', '=','#','`', '~', '|', '/', '\\', '\n', '\t', '\r'} ;
		
		for(int i = 0 ; i < m_ingoreChars.length ; i++){
			ingoreChars[m_ingoreChars[i]] = true ;
		}
	}
	
	public void reloadDicMap(String groupName){
		this.dicMap.remove(groupName) ;
	}
	
	protected synchronized Dictionary loadDictionary(String groupName){
		Dictionary d = this.dicMap.get(groupName) ;
		
		if(d == null){
			FilterWordGroup fwg = filterWordGroupManager.getById(groupName) ;
			if(fwg == null){
				return null ;
			}
			
			List<FilterWord> words = this.filterWordManager.listBadWordsByGroup(groupName) ;
			
			d = new Dictionary(fwg.getColor()) ;
			
			for(FilterWord w : words){
				d.addWord(w.getId(), w.getWord(), w.getLevel());
			}
			
			this.dicMap.put(groupName, d) ;
			this.versionControlService.register(Constants.buildVersionControlPath(Constants.serviceName.FILTER_WORD, groupName), d.getMaxWordId(), this) ;
		}
		
		return d ;
	}

	public void onNewVersion(String path, long localVersion, long newVersion) {
		String groupName = Constants.extractServiceKeyFromVersionControlPath(Constants.serviceName.FILTER_WORD, path) ;
		Dictionary d = this.dicMap.get(groupName) ;
		
		if(d != null){
			List<FilterWord> words = this.filterWordManager.listBadWords(groupName, (int) localVersion, (int) newVersion) ;
						
			for(FilterWord w : words){
				d.addWord(w.getId(), w.getWord(), w.getLevel());
			}
		}
	}

	public void onVersionDeleted(String path, long localVersion) {
		String groupName = Constants.extractServiceKeyFromVersionControlPath(Constants.serviceName.FILTER_WORD, path) ;
		
		this.reloadDicMap(groupName) ;
	}

	/**
	 * 
	 * @param content 要过滤得内容
	 * @param groupNames[] 过滤的组，如果仅使用系统过滤词表不分组，传入null
	 * @param markContent 是否标红要过滤的内容
	 * @param markFilterWordInHtmlTag 是否将html代码中的过滤词标红
	 * @param recordFilterWordInHtmlTag 是否将html代码中的过滤词记录下来
	 */
	public MatchResult filter(String content, String[] groupNames, boolean markContent, boolean markFilterWordInHtmlTag, boolean recordFilterWordInHtmlTag){
		if(LOG.isDebugEnabled()){
			LOG.debug("filter content:" + content) ;
		}
		
		if (StringUtil.isEmpty(content)) {
			return null ;
		}
		
		if(groupNames == null) return null ;

		// 根据词典组名称加载相应的过滤词典
		Dictionary[] subDictionaries = new Dictionary[groupNames.length] ;
		
		int startDicIndex = -1 ;
		Dictionary dictionary = null ;
		int highestLevel = 0 ;
		
		//把分组过滤词典加载进来。
		for(int i = 0 ; i < groupNames.length ; i ++){
			String m_groupName = groupNames[i] ;
			
			if(m_groupName == null){ //will cause NullPointerException in HashMap
				continue ;
			}
				
			Dictionary m_dictionary = loadDictionary(m_groupName) ;
				
			if(m_dictionary == null){ // this is a error!!!
				throw new IllegalParameterException("filterword group not found. group name:" + m_groupName) ;
			}else if(m_dictionary.isEmpty()){
				//空词典是没有意义的，不加入。
			}else{
				subDictionaries[i] = m_dictionary ;
				if(startDicIndex == -1){
					startDicIndex = i ;
					dictionary = m_dictionary ;
				}
			}
		}
		
		if(dictionary == null){
			//所有词典都没有录入过滤词
			return null ;
		}

		// 初始化
		LinkedList<String> filterWords = null;
		boolean hitted = false;
		int contentLength = content.length();
		StringBuffer markedContentResult = markContent ? new StringBuffer(contentLength + 100) : null;
		StringBuffer hittedContentList = null ;
		char ch;
		
		//跳过html标签部分
		boolean skipHtmlTag = !markFilterWordInHtmlTag && !recordFilterWordInHtmlTag ;
		
		//已经遇到了多少个"<"
		int ltDeep = 0 ;
		
		//当前字符是否在html标记中
		boolean inHtmlTagNow = false ;

		// 在指定字符串内查找过滤字
		for (int posOfContent = 0; posOfContent < contentLength;) {
			char org_ch = content.charAt(posOfContent) ;
			ch = Character.toLowerCase(org_ch) ;
			
			if(ch == '<'){
				ltDeep++ ;
				inHtmlTagNow = true ;
			}
			
			if(inHtmlTagNow){
				if(ch == '>'){
					ltDeep-- ;
					if(ltDeep < 1){
						inHtmlTagNow = false ;
					}
				}
			}
			
			//from startDic
			Dictionary m_dic = dictionary ;
			
			Word fw = m_dic.getWord(ch);
			hitted = false;
			
			//下一个使用的分组词典的序号
			int subDicIndex = startDicIndex + 1 ;
			
			if(ch <= 65534 && !ingoreChars[ch]){ //过滤词范围内
				
				//如果在html标签中，并且可以跳过标签，就直接跳过去。省去无意义的过滤词检查开销。
				if(inHtmlTagNow && skipHtmlTag){
					continue ;
				}
				
				while(fw == null){ //系统词典里面没有词，更换到下一个词典
					
					if(subDicIndex >= subDictionaries.length){
						break ;
					}
					
					m_dic = subDictionaries[subDicIndex] ;
					
					if(m_dic != null){
						fw = m_dic.getWord(ch) ;
					}
					
					subDicIndex++ ;	
				}
				
				while (fw != null) {
					String hittedContent = getMatchedContent(fw, content, posOfContent) ;
									
					if (hittedContent != null) {
	
						if(!inHtmlTagNow || recordFilterWordInHtmlTag){							
									
							if (filterWords == null){
								filterWords = new LinkedList<String>();
							}
							
							filterWords.add(fw.getWord()) ;
							highestLevel = Math.max(highestLevel, fw.getLevel()) ;
							
							if(hittedContentList == null){
								hittedContentList =  new StringBuffer(128) ;
								hittedContentList.append(hittedContent) ;
							}else{
								hittedContentList.append(',') ;
								hittedContentList.append(hittedContent) ;
							}
						}
	
						if (markContent) {
							if(!inHtmlTagNow || markFilterWordInHtmlTag){
								markedContentResult.append(wordMarkPrefixStartTag);
								markedContentResult.append(m_dic.getColor());
								markedContentResult.append(wordMarkPrefixEndTag);
								markedContentResult.append(hittedContent) ;
								markedContentResult.append(wordMarkSuffix);
							}else{
								markedContentResult.append(hittedContent) ;
							}
						}
	
						posOfContent = posOfContent + hittedContent.length();
						
						hitted = true;
						break;
					}
	
					fw = fw.getNext();
					
					while(fw == null){ //一个词典用完了，没有发现过滤词；更换到下一个词典
						
						if(subDicIndex >= subDictionaries.length){
							break ;
						}
						
						m_dic = subDictionaries[subDicIndex] ;
						
						if(m_dic != null){
							fw = m_dic.getWord(ch) ;
						}
						
						subDicIndex++ ;	
					}
				}
			}

			if (!hitted) {
				if (markContent)
					markedContentResult.append(org_ch);
				posOfContent++;
			}
		}

		if (filterWords != null) {
			if(LOG.isDebugEnabled()){
				LOG.debug("matched filter word:" + hittedContentList) ;
			}
			
			return new MatchResult(filterWords, markedContentResult == null ? null : markedContentResult.toString(), hittedContentList.toString(), highestLevel) ;
		}
		
		if(LOG.isDebugEnabled()){
			LOG.debug("no match.") ;
		}
		
		return null;
	}
	
	/**
	 * 判断是否含有一个过滤词，如果有返回命中的文本。如果没有返回null。
	 */
	protected String getMatchedContent(Word filterWord, String content, int startPosOfContent){
		String filterWordContent = filterWord.getWord() ;
		
		int lengthOfContent = content.length() ;
		
		boolean lastChar = startPosOfContent == lengthOfContent ;
			
		boolean m_hitted = true ;		
		int posOfFilterWord = 0, posOfContentBasedScanPos = 0 ;
		int pos_of_content = startPosOfContent + posOfContentBasedScanPos ;
		
		
		
		for(posOfFilterWord = 0, posOfContentBasedScanPos = 0 ; posOfFilterWord < filterWordContent.length() ;){
			pos_of_content = startPosOfContent + posOfContentBasedScanPos ;
			
			if(pos_of_content >= lengthOfContent){ //结束了
				break ;
			}
			
			char m_word = Character.toLowerCase(content.charAt(pos_of_content)) ;
			if(m_word > 65534){ //不再过滤词范围内
				posOfContentBasedScanPos++ ;
				continue ;
			}
			
			if(ingoreChars[m_word]){
				posOfContentBasedScanPos++ ;
				continue ;
			}
			if(filterWordContent.charAt(posOfFilterWord) != m_word){
				m_hitted = false ;
				break ;
			}
			
			posOfFilterWord++ ;
			posOfContentBasedScanPos++ ;
		}
		
		if(m_hitted){ //命中了，看看是不是content结束而造成了匹配一半循环结束报告命中。
			if(pos_of_content == lengthOfContent && posOfFilterWord < filterWordContent.length() ){
				return null ;
			}
		}
		
		if(m_hitted){ //命中了, 读取命中的文本.
			if(startPosOfContent > pos_of_content && !lastChar){ // this is a error
				LOG.error("error found! IWordFilter failed to match content!(" + startPosOfContent + "-->" + pos_of_content + ")", new Exception()) ;
				return null ;
			}
			
			return content.substring(startPosOfContent, pos_of_content + 1) ;
		}
		
		return null ;
	}
	
	public String getStopChars() {
		return stopChars;
	}

	public void setStopChars(String stopChars) {
		this.stopChars = stopChars;
		
		if(stopChars == null){
			return ;
		}
		
		for(int i = 0 ; i < stopChars.length() ; i++){
			char ch = stopChars.charAt(i) ;
			
			if(ch < 65535){
				ingoreChars[ch] = true ;
			}
		}
	}

	public String executeCommand(String command, String param) throws Exception {
		WordFilterRequest r = JsonUtil.fromJson(param, WordFilterRequest.class) ;
		MatchResult result = null ;
		
		if(WordFilterRequest.COMMAND_FILTER_HTML.equals(command)){
			result = this.filter(r.getContent(), r.getGroupNames(), r.isMarkContent(), false, true) ;
		}else if(WordFilterRequest.COMMAND_FILTER_TEXT.equals(command)){
			result = this.filter(r.getContent(), r.getGroupNames(), r.isMarkContent(), true, true) ;
		}else{
			throw new UnsupportedOperationException(command) ;
		}
		
		if(result == null) return null ;
		
		return JsonUtil.toJson(result) ;
	}

	public byte[] executeCommand(String command, byte[] param) throws Exception {
		throw new NoSuchMethodException("not supported!") ;
	}
	
	public void setCommandServerService(CommandServerService commandServerService) {
		commandServerService.addCommandHandler(WordFilterRequest.COMMAND_FILTER_HTML, this) ;
		commandServerService.addCommandHandler(WordFilterRequest.COMMAND_FILTER_TEXT, this) ;
	}

	public void setFilterWordManager(IFilterWordManager filterWordManager) {
		this.filterWordManager = filterWordManager;
	}

	public void setFilterWordGroupManager(IFilterWordGroupManager filterWordGroupManager) {
		this.filterWordGroupManager = filterWordGroupManager;
	}

	public void setVersionControlService(VersionControlService versionControlService) {
		this.versionControlService = versionControlService;
	}
	
}
