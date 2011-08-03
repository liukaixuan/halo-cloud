/**
 * 
 */
package com.guzzservices.manager;

/**
 * 
 * 
 * 
 * @author liukaixuan(liukaixuan@gmail.com)
 */
public class Constants {
	
	public static final _SERVICE_NAME_ serviceName = new _SERVICE_NAME_() ;
	
	public static class _SERVICE_NAME_{
		
		public final String FILTER_WORD = "gs_fw" ;
		
		public final String CONFIGURATION = "gs_config" ;
		
		public final String STAT_ITEM = "gs_si" ;
		
		public final String TASK = "gs_task" ;
		
		public final String IP_TABLE = "gs_ipt" ;
		
		public final String APP_LOG = "gs_alog" ;
		
	}
	
	public static String buildVersionControlPath(String serviceName, String serviceKey){
		StringBuilder sb = new StringBuilder() ;
		sb.append('/')
		  .append(serviceName)
		  .append('/')
		  .append(serviceKey) ;
		
		return sb.toString() ;
	}
	
	public static String extractServiceKeyFromVersionControlPath(String serviceName, String vcPath){
		String prefix = '/' + serviceName + '/' ;
		
		return vcPath.substring(prefix.length()) ;
	}

}
