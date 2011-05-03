/**
 * 
 */
package com.guzzservices.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.guzz.util.FileUtil;

/**
 * 
 * 
 * 
 * @author liukaixuan(liukaixuan@gmail.com)
 */
public class HttpClientUtils {
	
	static final String header = "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.2; SV1; .NET CLR 1.1.4322; CIBA; .NET CLR 2.0.50727; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)" ;
		
	public static String post(String url, Map<String, String> params, String encoding) throws IOException{
		DefaultHttpClient httpclient = new DefaultHttpClient();
		httpclient.getParams().setParameter("User-Agent", header) ;
		httpclient.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.BROWSER_COMPATIBILITY);
		
		HttpResponse response = null ;
		HttpEntity entity = null;
			
		//Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.1; Trident/4.0; CIBA; .NET CLR 1.1.4322; .NET CLR 2.0.50727)
		HttpPost httpost = new HttpPost(url);
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		
		if(params != null){
			for(Map.Entry<String, String> e : params.entrySet()){
				nvps.add(new BasicNameValuePair(e.getKey(), e.getValue()));
			}
		}
		
		UrlEncodedFormEntity en = new UrlEncodedFormEntity(nvps, encoding) ;
		
		httpost.setEntity(en);
		response = httpclient.execute(httpost);
		entity = response.getEntity();
			
		String content = null ;
	
		if (entity != null) {
			content = FileUtil.readText(entity.getContent(), encoding) ;
		}
		
		httpost.abort() ;
													
		return content ;
	}
	
	public static String get(String url, Map<String, String> params, String encoding) throws IOException{
		DefaultHttpClient httpclient = new DefaultHttpClient();
		httpclient.getParams().setParameter("User-Agent", header) ;
		
		if(params != null){
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			
			for(Map.Entry<String, String> e : params.entrySet()){
				nvps.add(new BasicNameValuePair(e.getKey(), e.getValue()));
			}
			
			if(!url.endsWith("?")){
				url += "?";
			}
			
			String paramString = URLEncodedUtils.format(nvps, "utf-8");
		    url += paramString;
		}
		
	    HttpGet g = new HttpGet(url);
	    HttpResponse response = httpclient.execute(g);
		HttpEntity entity = response.getEntity();

		String content = null ;

		if (entity != null) {
			content = FileUtil.readText(entity.getContent(), encoding) ;
		}
		
		g.abort() ;
		
		return content ;
	}

}
