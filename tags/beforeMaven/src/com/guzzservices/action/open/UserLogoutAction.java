/**
 * 
 */
package com.guzzservices.action.open;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.guzzservices.manager.ISessionManager;


/**
 * 
 * logout
 * 
 * @author liukaixuan(liukaixuan@gmail.com)
 */
public class UserLogoutAction implements Controller {
	
	private ISessionManager sessionManager ;
	
	private String loginView = "redirect:login.jsp" ;
	
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		sessionManager.logout(request, response) ;
	
		return new ModelAndView(loginView) ;
	}

	public ISessionManager getSessionManager() {
		return sessionManager;
	}

	public void setSessionManager(ISessionManager sessionManager) {
		this.sessionManager = sessionManager;
	}

}
