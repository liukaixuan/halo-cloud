package com.guzzservices.action.console.fw;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.guzzservices.manager.Constants;
import com.guzzservices.manager.IFilterWordGroupManager;
import com.guzzservices.manager.ISessionManager;
import com.guzzservices.sso.LoginUser;

/**
 * 重新加载一个过滤词组。
 * 
 * @author liu kaixuan
 */
public class FilterWordReloadAction implements Controller {
	
	private IFilterWordGroupManager filterWordGroupManager ;
	private ISessionManager sessionManager ;
	private String successView ;

	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		LoginUser loginUser = sessionManager.getLoginUser(request, response) ;
		String groupId = request.getParameter("groupId") ;
		
		//check permission
		this.sessionManager.assertCommiter(loginUser, Constants.serviceName.FILTER_WORD, groupId) ;
		
		this.filterWordGroupManager.reload(groupId) ;
		
		return new ModelAndView(getSuccessView(), "groupId", groupId) ;
	}

	public ISessionManager getSessionManager() {
		return sessionManager;
	}

	public void setSessionManager(ISessionManager sessionManager) {
		this.sessionManager = sessionManager;
	}

	public IFilterWordGroupManager getFilterWordGroupManager() {
		return filterWordGroupManager;
	}

	public void setFilterWordGroupManager(IFilterWordGroupManager filterWordGroupManager) {
		this.filterWordGroupManager = filterWordGroupManager;
	}

	public String getSuccessView() {
		return successView;
	}

	public void setSuccessView(String successView) {
		this.successView = successView;
	}

}
