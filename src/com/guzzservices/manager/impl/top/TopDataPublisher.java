package com.guzzservices.manager.impl.top;

import com.guzzservices.business.StatItem;

/**
 * 发布数据
 * 
 * @author liu kaixuan
 */
public interface TopDataPublisher {

	/**
	 * 发布数据
	 * 
	 * @return 数据接收方的返回内容
	 * */
	public String publishData(StatItem item, String text) throws Exception ;

}
