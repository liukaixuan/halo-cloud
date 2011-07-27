/**
 * 
 */
package com.guzzservices.action.console.log;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.guzz.util.RequestUtil;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.guzzservices.business.LogApp;
import com.guzzservices.manager.Constants;
import com.guzzservices.manager.ISessionManager;
import com.guzzservices.manager.ILogAppManager;
import com.guzzservices.sso.LoginUser;
import com.guzzservices.util.RandomUtils;

/**
 * 
 * 
 * 
 * @author liukaixuan(liukaixuan@gmail.com)
 */
public class LogAppAction implements Controller {
	
	private ILogAppManager logAppManager ;
	
	private ISessionManager sessionManager ;

	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		LoginUser loginUser = sessionManager.getLoginUser(request, response) ;
		int id = RequestUtil.getParameterAsInt(request, "id", 0) ;
		String appName = request.getParameter("appName") ;
		
		if(id > 0){
			this.sessionManager.assertOwner(loginUser, Constants.serviceName.APP_LOG, String.valueOf(id)) ;
			
			//edit
			LogApp m_group = this.logAppManager.getForUpdate(id) ;
			
			if(m_group == null){
				return null ;
			}
			
			m_group.setAppName(appName) ;
			
			this.logAppManager.update(m_group) ;
		}else{
			LogApp m_group = new LogApp() ;
			m_group.setCreatedTime(new Date()) ;
			m_group.setAppName(appName) ;
			m_group.setSecureCode(RandomUtils.generateRandomString(64)) ;
			
			this.logAppManager.add(loginUser, m_group) ;
		}
		
		return null;
	}

	public ILogAppManager getLogAppManager() {
		return logAppManager;
	}

	public void setLogAppManager(ILogAppManager logAppManager) {
		this.logAppManager = logAppManager;
	}

	public ISessionManager getSessionManager() {
		return sessionManager;
	}

	public void setSessionManager(ISessionManager sessionManager) {
		this.sessionManager = sessionManager;
	}

}
