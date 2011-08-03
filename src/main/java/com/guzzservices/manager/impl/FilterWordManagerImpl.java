/**
 * 
 */
package com.guzzservices.manager.impl;

import java.util.List;
import java.util.Set;

import org.guzz.dao.GuzzBaseDao;
import org.guzz.orm.se.SearchExpression;
import org.guzz.orm.se.Terms;
import org.guzz.transaction.WriteTranSession;

import com.guzzservices.business.FilterWord;
import com.guzzservices.manager.Constants;
import com.guzzservices.manager.IFilterWordManager;
import com.guzzservices.version.VersionControlService;

/**
 * 
 * 
 * 
 * @author liukaixuan(liukaixuan@gmail.com)
 */
public class FilterWordManagerImpl extends GuzzBaseDao implements IFilterWordManager {

	private VersionControlService versionControlService ;
		
	public void add(FilterWord word) {
		super.insert(word) ;
		
		this.versionControlService.upgradeVersionTo(Constants.buildVersionControlPath(Constants.serviceName.FILTER_WORD, word.getGroupId()), word.getId()) ;
	}

	public void addImport(String groupId, Set<FilterWord> words) {
		WriteTranSession write = this.getTransactionManager().openRWTran(false) ;
		int maxWordId = 0 ;
		try{
			for(FilterWord word : words){
				word.setGroupId(groupId) ;
				write.insert(word) ;
				
				maxWordId = word.getId() ;
			}
			
			write.commit() ;
		}catch(Exception e){
			write.rollback() ;
		}finally{
			write.close() ;
		}
		
		this.versionControlService.upgradeVersionTo(Constants.buildVersionControlPath(Constants.serviceName.FILTER_WORD, groupId), maxWordId) ;
	}

	public FilterWord getById(int id) {
		return (FilterWord) super.getForUpdate(FilterWord.class, id) ;
	}

	public FilterWord getByWord(String groupId, String word) {
		SearchExpression se = SearchExpression.forClass(FilterWord.class) ;
		se.and(Terms.eq("groupId", groupId)) ;
		se.and(Terms.like("word", "%" + word + "%", true)) ;
		
		return (FilterWord) super.findObject(se) ;
	}

	@SuppressWarnings("unchecked")
	public List<FilterWord> listBadWordsByGroup(String groupId) {
		SearchExpression se = SearchExpression.forClass(FilterWord.class, 1, 2000) ;
		se.and(Terms.eq("groupId", groupId)) ;
		
		//高等级的排到前面，一方面方便管理，另外一方面在WordFilter里面靠前，不会因为检测到低优先级的过滤词而忽略了此词。如"a"，"ab"都是过滤词。
		se.setOrderBy("level desc") ;
		
		return super.list(se) ;
	}
	
	public List<FilterWord> listBadWords(String groupId, int startId, int endId){
		SearchExpression se = SearchExpression.forClass(FilterWord.class, 1, 2000) ;
		se.and(Terms.eq("groupId", groupId)) ;
		se.and(Terms.bigger("id", startId)) ;
		se.and(Terms.smallerOrEq("id", endId)) ;
		
		return super.list(se) ;
	}

	public void remove(FilterWord word) {
		super.delete(word) ;
		
		this.versionControlService.deleteVersion(Constants.buildVersionControlPath(Constants.serviceName.FILTER_WORD, word.getGroupId())) ;
	}

	public void update(FilterWord word) {
		super.update(word) ;
		
		this.versionControlService.deleteVersion(Constants.buildVersionControlPath(Constants.serviceName.FILTER_WORD, word.getGroupId())) ;
	}

	public VersionControlService getVersionControlService() {
		return versionControlService;
	}

	public void setVersionControlService(VersionControlService versionControlService) {
		this.versionControlService = versionControlService;
	}

}
