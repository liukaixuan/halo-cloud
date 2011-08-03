package com.guzzservices.manager.impl;

import org.guzz.dao.GuzzBaseDao;
import org.guzz.transaction.WriteTranSession;

import com.guzzservices.business.TaskGroup;
import com.guzzservices.manager.Constants;
import com.guzzservices.manager.IAuthManager;
import com.guzzservices.manager.ITaskGroupManager;
import com.guzzservices.sso.LoginUser;

/**
 * 
 * 
 * @author liu kaixuan
 */
public class TaskGroupManagerImpl extends GuzzBaseDao implements ITaskGroupManager {
	
	private IAuthManager authManager ;

	public void add(LoginUser loginUser, TaskGroup group) {
		WriteTranSession write = this.getTransactionManager().openRWTran(false) ;
		
		try{
			write.insert(group) ;
			
			authManager.newAuth(write, loginUser.getUserName(), Constants.serviceName.TASK, String.valueOf(group.getId())) ;
			
			write.commit() ;
		}catch(RuntimeException e){
			write.rollback() ;
			
			throw e ;
		}finally{
			write.close() ;
		}
	}

	public TaskGroup getForRead(int id) {
		return (TaskGroup) super.getForRead(TaskGroup.class, id) ;
	}

	public TaskGroup getForUpdate(int id) {
		return (TaskGroup) super.getForUpdate(TaskGroup.class, id) ;
	}

	public IAuthManager getAuthManager() {
		return authManager;
	}

	public void setAuthManager(IAuthManager authManager) {
		this.authManager = authManager;
	}

}
