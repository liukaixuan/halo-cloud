/**
 * IFilterWordDao.java. created on 2006-7-13  
 */
package com.guzzservices.manager;

import java.util.List;
import java.util.Set;

import com.guzzservices.business.FilterWord;

/**
 * 过滤词DAO
 * 
 * @author liu kaixuan
 */
public interface IFilterWordManager{
	
	public void add(FilterWord word) ;
	
	public void addImport(String groupId, Set<FilterWord> words) ;
	
	public void update(FilterWord word) ;
	
	public void remove(FilterWord word) ;
		
	public FilterWord getById(int id) ;
	
	/**
	 * 通过过滤词内容读取过滤词，如果不存在返回null。
	 * @param groupId 组ID
	 * @param word 过滤词
	 */
	public FilterWord getByWord(String groupId, String word) ;
	
	/**
	 * 根据组名称提取一个分组下的所有过滤词。
	 * @param groupId 分组id。
	 */
	public List<FilterWord> listBadWordsByGroup(String groupId) ;
	
	/**
	 * 列出id在startId（不包含）和endId(包含)之间的某一组的过滤词。
	 */
	public List<FilterWord> listBadWords(String groupId, int startId, int endId) ;
	
}
