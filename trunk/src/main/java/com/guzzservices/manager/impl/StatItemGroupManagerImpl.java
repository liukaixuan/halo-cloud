package com.guzzservices.manager.impl;

import org.guzz.dao.GuzzBaseDao;
import org.guzz.transaction.WriteTranSession;

import com.guzzservices.business.StatItemGroup;
import com.guzzservices.manager.Constants;
import com.guzzservices.manager.IAuthManager;
import com.guzzservices.manager.IStatItemGroupManager;
import com.guzzservices.sso.LoginUser;

/**
 * 
 * 
 * @author liu kaixuan
 */
public class StatItemGroupManagerImpl extends GuzzBaseDao implements IStatItemGroupManager {
	
	private IAuthManager authManager ;

	public void add(LoginUser loginUser, StatItemGroup group) {
		WriteTranSession write = this.getTransactionManager().openRWTran(false) ;
		
		try{
			write.insert(group) ;
			
			authManager.newAuth(write, loginUser.getUserName(), Constants.serviceName.STAT_ITEM, String.valueOf(group.getId())) ;
			
			write.commit() ;
		}catch(RuntimeException e){
			write.rollback() ;
			
			throw e ;
		}finally{
			write.close() ;
		}
	}

	public StatItemGroup getForRead(int id) {
		return (StatItemGroup) super.getForRead(StatItemGroup.class, id) ;
	}

	public StatItemGroup getForUpdate(int id) {
		return (StatItemGroup) super.getForUpdate(StatItemGroup.class, id) ;
	}

	public IAuthManager getAuthManager() {
		return authManager;
	}

	public void setAuthManager(IAuthManager authManager) {
		this.authManager = authManager;
	}

}
