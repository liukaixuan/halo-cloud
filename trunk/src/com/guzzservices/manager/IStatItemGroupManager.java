package com.guzzservices.manager;

import com.guzzservices.business.StatItemGroup;
import com.guzzservices.sso.LoginUser;

/**
 * 
 * 
 * @author liu kaixuan
 */
public interface IStatItemGroupManager extends IBaseManager<StatItemGroup> {	

	public void add(LoginUser loginUser, StatItemGroup group) ;
	
}
