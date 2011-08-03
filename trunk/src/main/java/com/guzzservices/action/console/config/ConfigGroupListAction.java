/**
 * FilterWordPostAction.java. created on 2006-7-13  
 */
package com.guzzservices.action.console.config;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.guzzservices.manager.Constants;
import com.guzzservices.manager.IAuthManager;
import com.guzzservices.manager.ISessionManager;
import com.guzzservices.sso.LoginUser;

/**
 * 
 * list authed groups.
 * 
 * @author liu kaixuan
 */
public class ConfigGroupListAction implements Controller {
	
	private ISessionManager sessionManager ;
	
	private IAuthManager authManager ;

	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		LoginUser loginUser = sessionManager.getLoginUser(request, response) ;
		
		List<String> groupIds = this.authManager.listAuthedServiceKeys(loginUser.getUserName(), Constants.serviceName.CONFIGURATION) ;
		
		return new ModelAndView("/console/config/configGroupList", "groupIds", groupIds);
	}

	public ISessionManager getSessionManager() {
		return sessionManager;
	}

	public void setSessionManager(ISessionManager sessionManager) {
		this.sessionManager = sessionManager;
	}

	public IAuthManager getAuthManager() {
		return authManager;
	}

	public void setAuthManager(IAuthManager authManager) {
		this.authManager = authManager;
	}

}
