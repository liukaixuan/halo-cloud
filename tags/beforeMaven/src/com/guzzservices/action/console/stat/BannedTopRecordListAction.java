package com.guzzservices.action.console.stat;

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
 * 
 * @author liukaixuan(liukaixuan@gmail.com)
 */
public class BannedTopRecordListAction implements Controller {
	
	private ISessionManager sessionManager ;

	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		LoginUser loginUser = sessionManager.getLoginUser(request, response) ;
		String groupId = request.getParameter("groupId") ;
		
		this.sessionManager.assertCommiter(loginUser, Constants.serviceName.STAT_ITEM, groupId) ;
		
		return new ModelAndView("/console/stat/bannedTopRecordList", "groupId", groupId);
	}

	public ISessionManager getSessionManager() {
		return sessionManager;
	}

	public void setSessionManager(ISessionManager sessionManager) {
		this.sessionManager = sessionManager;
	}

}
