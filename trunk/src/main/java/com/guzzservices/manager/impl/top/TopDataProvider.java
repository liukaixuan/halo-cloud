package com.guzzservices.manager.impl.top;

import java.util.List;

import com.guzzservices.business.StatItem;
import com.guzzservices.business.TopRecord;
import com.guzzservices.manager.IStatItemManager;
import com.guzzservices.manager.ITopRecordManager;

/**
 * TopRecord的数据提供者
 * 
 * @author liu kaixuan
 */
public interface TopDataProvider {
	
	/**
	 * 刷新一个统计项的数据
	 */
	public List<TopRecord> readNewData(ITopRecordManager topRecordManager, IStatItemManager statItemManager, StatItem item) throws Exception ;
	
}
