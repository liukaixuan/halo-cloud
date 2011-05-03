/**
 * 
 */
package com.guzzservices.rpc.util;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.guzz.exception.GuzzException;

/**
 * 
 * 
 * 
 * @author liukaixuan(liukaixuan@gmail.com)
 */
public class JsonUtil {
	
	private static ObjectMapper mapper = new ObjectMapper() ;
	
	static{
		mapper.configure(org.codehaus.jackson.map.DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false) ;
	}
	
	public static String toJson(Object obj){
		if(obj == null) return null ;
		if(obj instanceof String) return (String) obj ;
		
		try {
			return mapper.writeValueAsString(obj) ;
		} catch (JsonGenerationException e) {
			throw new GuzzException(String.valueOf(obj), e) ;
		} catch (JsonMappingException e) {
			throw new GuzzException(String.valueOf(obj), e) ;
		} catch (IOException e) {
			throw new GuzzException(String.valueOf(obj), e) ;
		}
	}
	 
	public static <T> T fromJson(String json, Class<T> classOfT){
		if(json == null) return null ;
		if(String.class.isAssignableFrom(classOfT)) return (T) json ;
		
		try {
			return mapper.readValue(json, classOfT) ;
		} catch (JsonParseException e) {
			throw new GuzzException(json, e) ;
		} catch (JsonMappingException e) {
			throw new GuzzException(json, e) ;
		} catch (IOException e) {
			throw new GuzzException(json, e) ;
		}
	}

}
