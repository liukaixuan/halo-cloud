/**
 * FilterWordPostAction.java. created on 2006-7-13  
 */
package com.guzzservices.action.console.fw;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.guzzservices.manager.Constants;
import com.guzzservices.manager.ISessionManager;
import com.guzzservices.sso.LoginUser;

/**
 * 
 * 
 * @author liu kaixuan
 */
public class FilterWordListAction implements Controller {
	
	private ISessionManager sessionManager ;

	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String groupId = request.getParameter("groupId") ;
		LoginUser loginUser = sessionManager.getLoginUser(request, response) ;
		
		sessionManager.assertCommiter(loginUser, Constants.serviceName.FILTER_WORD, groupId) ;
		
		return new ModelAndView("/console/filterWord/filterWordList", "groupId", groupId);
	}

	public ISessionManager getSessionManager() {
		return sessionManager;
	}

	public void setSessionManager(ISessionManager sessionManager) {
		this.sessionManager = sessionManager;
	}

}
