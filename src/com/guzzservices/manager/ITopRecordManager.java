package com.guzzservices.manager;

import java.util.List;

import org.guzz.transaction.WriteTranSession;

import com.guzzservices.business.BannedTopRecord;
import com.guzzservices.business.StatItem;
import com.guzzservices.business.TopRecord;

/**
 * 
 * 
 * @author liu kaixuan
 */
public interface ITopRecordManager extends IBaseManager<TopRecord> {

	public BannedTopRecord getBannedTopRecord(String objectId, int groupId);
	
	public void hideRecord(TopRecord record);
	
	public void showRecord(TopRecord record);

	public void sortRecords(StatItem item, boolean isAsending);
	
	public List<TopRecord> listCleanRecords(StatItem item, int maxSize);
	
	public void cleanUpOldData(WriteTranSession write, StatItem item);
	
}
