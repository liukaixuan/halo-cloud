/**
 * IFilterWordGroupDao.java. created on 2006-7-25  
 */
package com.guzzservices.manager;

import com.guzzservices.business.FilterWordGroup;
import com.guzzservices.sso.LoginUser;


/**
 * 
 * 
 * @author liu kaixuan
 * @date 2006-7-25 13:34:45
 */
public interface IFilterWordGroupManager {
	
	public void add(LoginUser loginUser, FilterWordGroup group) ;
	
	public void update(FilterWordGroup group) ;
	
	public void remove(FilterWordGroup group) ;
	
	public FilterWordGroup getById(String groupId) ;
	
}
