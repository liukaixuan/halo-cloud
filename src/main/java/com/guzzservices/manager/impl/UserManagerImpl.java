/**
 * 
 */
package com.guzzservices.manager.impl;

import org.guzz.dao.GuzzBaseDao;
import org.guzz.orm.se.SearchExpression;
import org.guzz.orm.se.Terms;

import com.guzzservices.business.User;
import com.guzzservices.manager.IUserManager;

/**
 * 
 * 
 * 
 * @author liukaixuan(liukaixuan@gmail.com)
 */
public class UserManagerImpl extends GuzzBaseDao implements IUserManager {

	public void addUser(User user) {
		super.insert(user) ;
	}

	public User getByEmail(String email) {
		SearchExpression se = SearchExpression.forClass(User.class) ;
		se.and(Terms.eq("email", email)) ;
		
		return (User) super.findObject(se) ;
	}

}
