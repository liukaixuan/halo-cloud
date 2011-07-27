/**
 * 
 */
package com.guzzservices.management;

import java.util.List;
import java.util.Map;

import org.guzz.dao.PageFlip;

/**
 * 
 * 
 * 
 * @author liukaixuan(liukaixuan@gmail.com)
 */
public interface AppLogService {
	
	/**
	 * 插入一条日志。
	 * 
	 * @param userId 操作用户
	 * @param customProps 日志自定义属性
	 */
	public void insertLog(int userId, Map<String, Object> customProps) throws Exception ;
	
	/**
	 * 
	 * 查询日志
	 * 
	 * @param conditions 条件列表。每条一个条件，如：userId=1，如：title~=读书
	 * @param orderBy 
	 * @param pageNo
	 * @param pageSize
	 * 
	 * @return 如果条件不足，可能返回null；如果条件错误，可能抛出异常。
	 */
	public PageFlip queryLogs(List<String> conditions, String orderBy, int pageNo, int pageSize) throws Exception ;

}
