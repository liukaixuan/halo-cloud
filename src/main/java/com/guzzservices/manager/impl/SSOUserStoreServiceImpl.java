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
	
	private static Map<Integer, String> errorMsgs = new HashMap<Integer, String>() ;
	
	static{
		errorMsgs.put(LoginException.IP_BANNED, "IP已被禁用！") ;
		errorMsgs.put(LoginException.NO_SUCH_USER, "用户名或密码错误！") ;
		errorMsgs.put(LoginException.PASSWORD_WRONG, "用户名或密码错误！") ;
		errorMsgs.put(LoginException.PERMISSION_DENIED, "无权限！") ;
		errorMsgs.put(LoginException.SERVER_INTERNAL_ERROR, "服务器内部错误！") ;
		errorMsgs.put(LoginException.USER_BANNED, "用户已被禁用！") ;
	}
	
	public String translateErrorCode(int errorCode) {
		return errorMsgs.get(errorCode) ;
	}
	
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

	public String queryUserName(int userId) {
		return null;
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
