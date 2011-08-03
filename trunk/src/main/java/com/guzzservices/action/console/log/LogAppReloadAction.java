/**
 * 
 */
package com.guzzservices.action.console.log;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.guzzservices.manager.Constants;
import com.guzzservices.manager.IAuthManager;
import com.guzzservices.manager.ILogAppManager;
import com.guzzservices.manager.ISessionManager;
import com.guzzservices.sso.LoginUser;

/**
 * 
 * 刷新日志版本到最新，使得应用重新加载自定义属性等信息。
 * 
 * @author liukaixuan(liukaixuan@gmail.com)
 */
public class LogAppReloadAction implements Controller {
	
	private ISessionManager sessionManager ;
	
	private ILogAppManager logAppManager ;

	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		LoginUser loginUser = sessionManager.getLoginUser(request, response) ;
		String appId = request.getParameter("appId") ;
		this.sessionManager.assertOwner(loginUser, Constants.serviceName.APP_LOG, appId) ;
		
		logAppManager.incVersion(Integer.valueOf(appId)) ;
		
		return new ModelAndView("redirect:/console/log/logCustomPropertyList.do", "appId", appId);
	}

	public ISessionManager getSessionManager() {
		return sessionManager;
	}

	public void setSessionManager(ISessionManager sessionManager) {
		this.sessionManager = sessionManager;
	}

	public ILogAppManager getLogAppManager() {
		return logAppManager;
	}

	public void setLogAppManager(ILogAppManager logAppManager) {
		this.logAppManager = logAppManager;
	}

}
