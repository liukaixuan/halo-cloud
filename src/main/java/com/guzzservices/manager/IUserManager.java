/**
 * 
 */
package com.guzzservices.manager;

import com.guzzservices.business.User;

/**
 * 
 * 
 * 
 * @author liukaixuan(liukaixuan@gmail.com)
 */
public interface IUserManager {
	
	public void addUser(User user) ;
	
	public User getByEmail(String email) ;

}
