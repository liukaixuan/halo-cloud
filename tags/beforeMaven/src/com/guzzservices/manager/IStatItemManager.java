package com.guzzservices.manager;

import com.guzzservices.business.StatItem;

/**
 * 
 * 
 * @author liu kaixuan
 */
public interface IStatItemManager extends IBaseManager<StatItem> {
	
	public void setException(StatItem item, Exception e);
	
	public String publishRecords(StatItem item);
	
	public void refreshRecords(StatItem item);
	
	public void reScheduleTask(StatItem si, String newCron) ;
	
}
