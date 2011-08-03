package com.guzzservices.manager.impl;

import org.guzz.dao.GuzzBaseDao;

import com.guzzservices.business.LogCustomProperty;
import com.guzzservices.manager.ILogCustomPropertyManager;

/**
 * 
 * 
 * @author liu kaixuan
 */
public class LogCustomPropertyManagerImpl extends GuzzBaseDao implements ILogCustomPropertyManager {
	
	public LogCustomProperty getForRead(int id) {
		return (LogCustomProperty) super.getForRead(LogCustomProperty.class, id) ;
	}

	public LogCustomProperty getForUpdate(int id) {
		return (LogCustomProperty) super.getForUpdate(LogCustomProperty.class, id) ;
	}

}
