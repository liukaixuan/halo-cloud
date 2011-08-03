/**
 * 
 */
package com.guzzservices.velocity.api;

import java.util.Map;

/**
 * 
 * 
 * 
 * @author liukaixuan(liukaixuan@gmail.com)
 */
public class LangUtil {
	
	public static int getIntParam(Map params, String key, int defaultValue){
		Object v = params.get(key) ;
		if(v == null) return defaultValue ;
		
		if(v instanceof Number){
			return ((Number) v).intValue() ;
		}else{
			return Integer.parseInt(v.toString()) ;
		}
	}

}
