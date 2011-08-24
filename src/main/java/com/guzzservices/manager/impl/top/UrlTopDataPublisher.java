/**
 * 
 */
package com.guzzservices.manager.impl.top;

import java.util.HashMap;

import com.guzzservices.business.StatItem;
import com.guzzservices.util.HttpClientUtils;

/**
 * 
 * 
 * 
 * @author liukaixuan(liukaixuan@gmail.com)
 */
public class UrlTopDataPublisher implements TopDataPublisher {

	public String publishData(StatItem item, String text) throws Exception {
		HashMap<String, String> params = new HashMap<String, String>() ;
		params.put("authKey", item.getAuthKey()) ;
		params.put("statId", String.valueOf(item.getId())) ;
		params.put("statName", item.getName()) ;
		params.put("time", String.valueOf(item.getStatBeforeMinutes())) ;
		params.put("programId", item.getProgramId()) ;
		params.put("content", text) ;
		
		return HttpClientUtils.post(item.getDataPublisherUrl(), params, item.getEncoding()) ;
	}

}
