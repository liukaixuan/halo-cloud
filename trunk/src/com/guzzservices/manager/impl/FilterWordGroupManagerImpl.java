/**
 * 
 */
package com.guzzservices.manager.impl;

import org.guzz.dao.GuzzBaseDao;
import org.guzz.transaction.WriteTranSession;

import com.guzzservices.business.FilterWordGroup;
import com.guzzservices.manager.Constants;
import com.guzzservices.manager.IAuthManager;
import com.guzzservices.manager.IFilterWordGroupManager;
import com.guzzservices.sso.LoginUser;
import com.guzzservices.version.VersionControlService;

/**
 * 
 * 
 * 
 * @author liukaixuan(liukaixuan@gmail.com)
 */
public class FilterWordGroupManagerImpl extends GuzzBaseDao implements IFilterWordGroupManager {
	
	private VersionControlService versionControlService ;
	
	private IAuthManager authManager ;

	public void add(LoginUser loginUser, FilterWordGroup group) {
		WriteTranSession write = this.getTransactionManager().openRWTran(false) ;
		
		try{
			write.insert(group) ;
			
			authManager.newAuth(write, loginUser.getUserName(), Constants.serviceName.FILTER_WORD, group.getId()) ;
			
			write.commit() ;
		}catch(RuntimeException e){
			write.rollback() ;
			
			throw e ;
		}finally{
			write.close() ;
		}
	}

	public FilterWordGroup getById(String groupId) {
		return (FilterWordGroup) super.getForUpdate(FilterWordGroup.class, groupId);
	}

	public void remove(FilterWordGroup group) {
		//禁止删除！避免填写的大量过滤词丢失。
	}

	public void update(FilterWordGroup group) {
		super.update(group) ;
		this.versionControlService.deleteVersion(Constants.buildVersionControlPath(Constants.serviceName.FILTER_WORD, group.getId())) ;
	}

	public VersionControlService getVersionControlService() {
		return versionControlService;
	}

	public void setVersionControlService(VersionControlService versionControlService) {
		this.versionControlService = versionControlService;
	}

	public IAuthManager getAuthManager() {
		return authManager;
	}

	public void setAuthManager(IAuthManager authManager) {
		this.authManager = authManager;
	}

}
