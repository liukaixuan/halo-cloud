/**
 * 
 */
package com.guzzservices.action.vo;

import com.guzzservices.business.Configuration;

/**
 * 
 * 
 * 
 * @author liukaixuan(liukaixuan@gmail.com)
 */
public class ConfigurationForm {

	private Configuration configuration ;
	
	private boolean newConfig ;
	
	public ConfigurationForm(Configuration configuration, String groupId){
		if(configuration != null){
			this.configuration = configuration ;
			this.newConfig = false ;
		}else{
			this.configuration = new Configuration() ;
			this.configuration.setGroupId(groupId) ;
			this.newConfig = true ;
		}
	}

	public Configuration getConfiguration() {
		return configuration;
	}

	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

	public boolean isNewConfig() {
		return newConfig;
	}

	public void setNewConfig(boolean newGroup) {
		this.newConfig = newGroup;
	}

}
