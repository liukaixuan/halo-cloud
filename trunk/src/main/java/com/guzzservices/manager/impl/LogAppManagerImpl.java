package com.guzzservices.manager.impl;

import java.util.List;

import org.guzz.dao.GuzzBaseDao;
import org.guzz.orm.se.SearchExpression;
import org.guzz.orm.se.Terms;
import org.guzz.transaction.WriteTranSession;

import com.guzzservices.business.LogApp;
import com.guzzservices.business.LogCustomProperty;
import com.guzzservices.manager.Constants;
import com.guzzservices.manager.IAuthManager;
import com.guzzservices.manager.ILogAppManager;
import com.guzzservices.sso.LoginUser;
import com.guzzservices.version.VersionControlService;

/**
 * 
 * 
 * @author liu kaixuan
 */
public class LogAppManagerImpl extends GuzzBaseDao implements ILogAppManager {
	
	private IAuthManager authManager ;
	
	private VersionControlService versionControlService ;

	public void add(LoginUser loginUser, LogApp app) {
		WriteTranSession write = this.getTransactionManager().openRWTran(false) ;
		
		try{
			write.insert(app) ;
			
			authManager.newAuth(write, loginUser.getUserName(), Constants.serviceName.APP_LOG, String.valueOf(app.getId())) ;
			
			write.commit() ;
		}catch(RuntimeException e){
			write.rollback() ;
			
			throw e ;
		}finally{
			write.close() ;
		}
	}
	
	public int getAppIdBySecureCode(String secureCode) {
		//TODO: add cache here.
		
		SearchExpression se = SearchExpression.forClass(LogApp.class) ;
		se.and(Terms.eq("secureCode", secureCode)) ;
		
		LogApp log = (LogApp) super.findObject(se) ;
		if(log == null) return -1 ;
		
		return log.getId() ;
	}

	public List<LogCustomProperty> listLogCustomProperties(int appId){
		SearchExpression se = SearchExpression.forClass(LogCustomProperty.class) ;
		se.and(Terms.eq("appId", appId)) ;
		
		return super.list(se) ;
	}

	public int getLastestVersion(int appId) {
		return (int) versionControlService.getVersion("/alog/" + appId) ;
	}
	
	public void incVersion(int appId){
		this.versionControlService.incVersion("/alog/" + appId) ;
	}

	public LogApp getForRead(int id) {
		return (LogApp) super.getForRead(LogApp.class, id) ;
	}

	public LogApp getForUpdate(int id) {
		return (LogApp) super.getForUpdate(LogApp.class, id) ;
	}

	public IAuthManager getAuthManager() {
		return authManager;
	}

	public void setAuthManager(IAuthManager authManager) {
		this.authManager = authManager;
	}

	public VersionControlService getVersionControlService() {
		return versionControlService;
	}

	public void setVersionControlService(VersionControlService versionControlService) {
		this.versionControlService = versionControlService;
	}

}
