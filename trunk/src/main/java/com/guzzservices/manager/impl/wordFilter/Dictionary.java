package com.guzzservices.manager.impl.wordFilter;

import org.apache.log4j.Logger;
import org.guzz.util.StringUtil;

/**
 * 过滤词引擎- 过滤词典实体对象
 */
final class Dictionary{
	protected static final Logger LOG = Logger.getLogger(Dictionary.class);
	
	/**
	 * 过滤词典索引数组
	 */
	private Word[] m_fwl = null;
	
	private String color ;
	
	private int maxWordId ;
	
	public Dictionary(String color) {
		this.color = color ;
		this.m_fwl = new Word[65535];
	}
	
	public Dictionary(String color, Word[] words){
		this.color = color ;
		this.m_fwl=(Word[]) words.clone();
	}
	
	//词典是否为空
	private boolean isEmpty = true ;
	
	public boolean isEmpty(){
		return isEmpty ;
	}

	/**
	 * 增加新的过滤词。将所有字母换成小写。
	 * 
	 * @param p_word 要加入的过滤词
	 */
	public void addWord(int wordId, String p_word, int level) {
		maxWordId = Math.max(maxWordId, wordId) ;
		
		if (!StringUtil.isEmpty(p_word)) {
			p_word = p_word.toLowerCase().trim() ;
			
			char index = p_word.charAt(0);
			
			Word word = new Word();
			word.setWord(p_word);
			word.setNext(this.m_fwl[index]);
			word.setLevel(level);
			this.m_fwl[index] = word;
			
			//设置词典包含过滤词
			isEmpty = false ;
			
			if(LOG.isDebugEnabled()){
				LOG.debug("加入过滤词:"+word);
			}
		}
	}

	/**
	 * 返回以指定的字符开始的过滤词列表, 如果没有返回 <code>null</code>
	 * 过滤词的第一个字母应该为小写
	 * @param p_ch 过滤词的第一个字符
	 */
	public Word getWord(char p_ch) {
		return this.m_fwl[p_ch];
	}	
	
	public  Dictionary getCloneDic(String newGroupName){
		return new Dictionary(newGroupName, this.m_fwl);
	}
	
	public void clear(){
		this.m_fwl = new Word[65535];
		this.isEmpty = true ;
	}

	public String getColor() {
		return color;
	}

	public int getMaxWordId() {
		return maxWordId;
	}

	public void setMaxWordId(int maxWordId) {
		this.maxWordId = maxWordId;
	}

	
}
