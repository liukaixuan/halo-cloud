/**
 * 
 */
package com.guzzservices.manager;

import java.util.List;

import com.guzzservices.business.LogApp;
import com.guzzservices.business.LogCustomProperty;
import com.guzzservices.sso.LoginUser;

/**
 * 
 * 
 * 
 * @author liukaixuan(liukaixuan@gmail.com)
 */
public interface ILogAppManager extends IBaseManager<LogApp> {

	public void add(LoginUser loginUser, LogApp logApp) ;
	
	public int getAppIdBySecureCode(String secureCode) ;
	
	public List<LogCustomProperty> listLogCustomProperties(int appId) ;
	
	public int getLastestVersion(int appId) ;
	
	public void incVersion(int appId) ;

}
