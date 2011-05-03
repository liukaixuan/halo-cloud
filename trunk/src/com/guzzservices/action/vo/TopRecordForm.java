package com.guzzservices.action.vo;

import com.guzzservices.business.TopRecord;

/**
 * 
 * 
 * @author liu kaixuan
 */
public class TopRecordForm {
	
	private TopRecord topRecord ;
	
	private boolean newTopRecord ;

	public TopRecordForm(TopRecord topRecord, boolean newTopRecord) {
		if(topRecord == null){
			this.topRecord = new TopRecord() ;
			this.newTopRecord = newTopRecord ;
		}else{
			this.topRecord = topRecord ;
			this.newTopRecord = newTopRecord ;
		}
	}

	public boolean isNewTopRecord() {
		return newTopRecord;
	}

	public void setNewTopRecord(boolean newTopRecord) {
		this.newTopRecord = newTopRecord;
	}

	public TopRecord getTopRecord() {
		return topRecord;
	}

	public void setTopRecord(TopRecord topRecord) {
		this.topRecord = topRecord;
	}
}
