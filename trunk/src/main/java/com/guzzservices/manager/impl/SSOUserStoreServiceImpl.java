/**
 * 
 */
package com.guzzservices.manager.impl;

import java.util.HashMap;
import java.util.Map;

import org.guzz.service.AbstractService;
import org.guzz.service.ServiceConfig;
import org.guzz.web.context.ExtendedBeanFactory;
import org.guzz.web.context.ExtendedBeanFactoryAware;

import com.guzzservices.business.User;
import com.guzzservices.manager.IUserManager;
import com.guzzservices.sso.LoginException;
import com.guzzservices.sso.LoginUser;
import com.guzzservices.sso.UserStoreService;
import com.guzzservices.sso.stub.SSOInfo;
import com.guzzservices.util.PasswordUtil;

/**
 * 
 * 
 * 
 * @author liukaixuan(liukaixuan@gmail.com)
 */
public class SSOUserStoreServiceImpl extends AbstractService implements UserStoreService, ExtendedBeanFactoryAware {

	private IUserManager userManager ;
	
	public int checkLogin(String userName, String password, String IP) {
		User user = userManager.getByEmail(userName) ;
			
		if(user == null) return LoginException.NO_SUCH_USER ;
		if(!user.getPassword().equals(PasswordUtil.md5(password))) return LoginException.PASSWORD_WRONG ;
		
		return SSOInfo.SUCCESS ;
	}

	public LoginUser getLoginUser(String userName) {
		User user = userManager.getByEmail(userName) ;
		
		LoginUser loginUser = new LoginUser() ;
		loginUser.setDisplayName(user.getNickName()) ;
		loginUser.setUserName(user.getEmail()) ;
		loginUser.setUserId(user.getId()) ;
		loginUser.setStatus(user.getStatus()) ;
		loginUser.setRoleId(user.isAdmin() ? 1 : 0);
		
		return loginUser ;
	}

	public int queryUserId(String userName) {
		User user = userManager.getByEmail(userName) ;
		
		return user == null ? -1 : user.getId() ;
	}

	public void setExtendedBeanFactory(ExtendedBeanFactory fac) {
		this.userManager = (IUserManager) fac.getBean("userManager") ;
	}

	public boolean isAvailable() {
		return userManager != null ;
	}

	public boolean configure(ServiceConfig[] scs) {
		return true;
	}

	public void shutdown() {		
	}

	public void startup() {		
	}

	public Map<String, Object> queryUserInfo(String userName) {
		User user = userManager.getByEmail(userName) ;
		if(user == null){
			return null ;
		}
		
		HashMap<String, Object> info = new HashMap<String, Object>() ;
		
		return info;
	}

}
