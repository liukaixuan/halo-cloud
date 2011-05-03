/**
 * 
 */
package com.guzzservices.action.open;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.guzzservices.manager.ISessionManager;
import com.guzzservices.sso.LoginException;


/**
 * 
 * login
 * 
 * @author liukaixuan(liukaixuan@gmail.com)
 */
public class UserLoginAction implements Controller {
	
	private ISessionManager sessionManager ;
	
	private String loginView = "redirect:login.jsp" ;

	private String successView = "redirect:console/default.jsp" ;
	
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String email = request.getParameter("userName") ;
		String password = request.getParameter("password") ;
		
		try{
			sessionManager.login(request, response, email, password) ;
		}catch(LoginException e){
			return new ModelAndView(loginView, "msg", "邮箱密码错误, code:" + e.getErrorCode()) ;
		}
		
		return new ModelAndView(successView) ;
	}

	public ISessionManager getSessionManager() {
		return sessionManager;
	}

	public void setSessionManager(ISessionManager sessionManager) {
		this.sessionManager = sessionManager;
	}


}
