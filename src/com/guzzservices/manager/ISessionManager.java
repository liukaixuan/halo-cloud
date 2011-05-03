/**
 * 
 */
package com.guzzservices.manager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.guzzservices.sso.LoginException;
import com.guzzservices.sso.LoginUser;

/**
 * 
 * 
 * 
 * @author liukaixuan(liukaixuan@gmail.com)
 */
public interface ISessionManager {
	
	public LoginUser getLoginUser(HttpServletRequest request, HttpServletResponse response) ;
	
	/**
	 * login for the current browser session.
	 */
	public void login(HttpServletRequest request, HttpServletResponse response, String userName, String password) throws LoginException ;
	
	public void logout(HttpServletRequest request, HttpServletResponse response) ;
	
	/**
	 * Is anyone logged in?
	 * <br>Not strict checking. May return a wrong result in hacking.
	 */
	public boolean isLogin(HttpServletRequest request, HttpServletResponse response) ;
	
	//ACL auth
	public boolean isOwner(LoginUser loginUser, String serviceName, String serviceKey) ;
	
	public boolean isCommiter(LoginUser loginUser, String serviceName, String serviceKey) ;
	
	public void assertOwner(LoginUser loginUser, String serviceName, String serviceKey) ;
	
	public void assertCommiter(LoginUser loginUser, String serviceName, String serviceKey) ;
	
	public void throwNoPermissionException(LoginUser loginUser, String serviceName, String serviceKey, String errorMsg) ;
		
}
