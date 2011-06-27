/**
 * 
 */
package com.guzzservices.action.console.task;

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
 * 
 * 
 * @author liukaixuan(liukaixuan@gmail.com)
 */
public class TaskGroupListAction implements Controller {
	
	private ISessionManager sessionManager ;
	
	private IAuthManager authManager ;

	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		LoginUser loginUser = sessionManager.getLoginUser(request, response) ;
		
		List<String> groupIds = this.authManager.listAuthedServiceKeys(loginUser.getUserName(), Constants.serviceName.TASK) ;
		
		return new ModelAndView("/console/task/taskGroupList", "groupIds", groupIds);
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
