package com.guzzservices.manager;

import com.guzzservices.secure.wordFilter.MatchResult;

/**
 * Filter bad words and mark it.
 *
 * @author liukaixuan
 */
public interface IWordFilter {

	public void reloadDicMap(String groupName) ;

	/**
	 * 过滤一段内容。如果不含有任何过滤词，返回null。
	 * 
	 * @param content 要过滤得内容
	 * @param groupNames[] 过滤的组，如果仅使用系统过滤词表不分组，传入null
	 * @param markContent 是否标红要过滤的内容
	 * @param markFilterWordInHtmlTag 是否将html代码中的过滤词标红
	 * @param recordFilterWordInHtmlTag 是否将html代码中的过滤词记录下来
	 */
	public MatchResult filter(String content, String[] groupNames, boolean markContent, boolean markFilterWordInHtmlTag, boolean recordFilterWordInHtmlTag) ;

	
}
