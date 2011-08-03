package com.guzzservices.action.vo;

import com.guzzservices.business.LogCustomProperty;

/**
 * 
 * 
 * @author liu kaixuan
 */
public class LogCustomPropertyForm {

	private LogCustomProperty property ;
	
	private boolean newProperty ;
		
	public LogCustomPropertyForm(LogCustomProperty property){
		newProperty = false ;
		this.property = property ;
	}
	
	public LogCustomPropertyForm(int appId){
		newProperty = true ;
		this.property = new LogCustomProperty() ;
		this.property.setAppId(appId) ;
		this.property.setDataType("string") ;
	}

	public LogCustomProperty getProperty() {
		return property;
	}

	public void setProperty(LogCustomProperty property) {
		this.property = property;
	}

	public boolean isNewProperty() {
		return newProperty;
	}

	public void setNewProperty(boolean newProperty) {
		this.newProperty = newProperty;
	}
	
}
