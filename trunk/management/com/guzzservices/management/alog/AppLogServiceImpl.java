/**
 * 
 */
package com.guzzservices.management.alog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.guzz.dao.PageFlip;
import org.guzz.service.AbstractService;
import org.guzz.service.ServiceConfig;
import org.guzz.util.Assert;

import com.guzzservices.management.AppLogService;
import com.guzzservices.rpc.CommandService;
import com.guzzservices.rpc.util.JsonPageFlip;
import com.guzzservices.rpc.util.JsonUtil;

/**
 * 
 * 
 * 
 * @author liukaixuan(liukaixuan@gmail.com)
 */
public class AppLogServiceImpl extends AbstractService implements AppLogService {
	
	public static final String COMMAND_NEW_LOG = "gs.alog.new.l" ;
	
	public static final String COMMAND_QUERY_LOG = "gs.qlog.q.l" ;
	
	public static final String COMMAND_QUERY_META = "gs.qlog.q.m" ;
	
	public static final String KEY_APP_SECURE_CODE = "__key_app_scode" ;
	
	public static final String KEY_APP_USER_ID = "__key_app_uid" ;
	
	private CommandService commandService ;
	
	private String secureCode ;
	
	public void insertLog(int userId, Map<String, Object> customProps) throws Exception{
		customProps.put(KEY_APP_SECURE_CODE, secureCode) ;
		customProps.put(KEY_APP_USER_ID, userId) ;
		
		this.commandService.executeCommand(COMMAND_NEW_LOG, JsonUtil.toJson(customProps)) ;
	}
	
	/**
	 * 如果条件不足，可能返回null；如果条件错误，可能抛出异常。
	 */
	public PageFlip queryLogs(List<String> conditions, String orderBy, int pageNo, int pageSize) throws Exception{
		AppLogQueryRequest r = new AppLogQueryRequest() ;
		r.setSecureCode(secureCode) ;
		r.setConditions(conditions) ;
		r.setOrderBy(orderBy) ;
		r.setPageNo(pageNo) ;
		r.setPageSize(pageSize) ;
		
		String json = this.commandService.executeCommand(COMMAND_QUERY_LOG, JsonUtil.toJson(r)) ;
		
		if(json == null) return null ;
		
		return JsonPageFlip.fromJson(json, LogRecord.class).toPageFlip() ;
	}

	public Map<String, String> queryCustomPropsMetaInfo() throws Exception {
		String json = this.commandService.executeCommand(COMMAND_QUERY_META, this.secureCode) ;
		
		return JsonUtil.fromJson(json, HashMap.class) ;
	}

	public boolean configure(ServiceConfig[] scs) {
		if(scs.length == 1){
			String secureCode = scs[0].getProps().getProperty("secureCode") ;
			Assert.assertNotEmpty(secureCode, "secureCode is a must!") ;
			
			this.secureCode = secureCode ;
			
			return true ;
		}
		
		return false;
	}

	public boolean isAvailable() {
		return secureCode != null ;
	}

	public void shutdown() {
	}

	public void startup() {
	}

	public CommandService getCommandService() {
		return commandService;
	}

	public void setCommandService(CommandService commandService) {
		this.commandService = commandService;
	}
	
	public static class AppLogQueryRequest{
		
		private String secureCode ;
		
		private int pageNo ;
		
		private int pageSize ;
		
		private String orderBy ;
		
		private List<String> conditions ;

		public String getSecureCode() {
			return secureCode;
		}

		public void setSecureCode(String secureCode) {
			this.secureCode = secureCode;
		}

		public List<String> getConditions() {
			return conditions;
		}

		public void setConditions(List<String> conditions) {
			this.conditions = conditions;
		}

		public int getPageNo() {
			return pageNo;
		}

		public void setPageNo(int pageNo) {
			this.pageNo = pageNo;
		}

		public int getPageSize() {
			return pageSize;
		}

		public void setPageSize(int pageSize) {
			this.pageSize = pageSize;
		}

		public String getOrderBy() {
			return orderBy;
		}

		public void setOrderBy(String orderBy) {
			this.orderBy = orderBy;
		}
		
	}

}
