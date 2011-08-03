/**
 * 
 */
package com.guzzservices.action.console.log;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.guzz.util.Assert;
import org.guzz.util.RequestUtil;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.guzzservices.business.LogCustomProperty;
import com.guzzservices.manager.Constants;
import com.guzzservices.manager.ILogCustomPropertyManager;
import com.guzzservices.manager.ISessionManager;
import com.guzzservices.sso.LoginUser;

/**
 * 
 * 
 * 
 * @author liukaixuan(liukaixuan@gmail.com)
 */
public class LogCustomPropertyDeleteAction implements Controller {
	
	private ISessionManager sessionManager ;

	private ILogCustomPropertyManager logCustomPropertyManager ;
	
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		LoginUser loginUser = sessionManager.getLoginUser(request, response) ;
		int id = RequestUtil.getParameterAsInt(request, "id") ;
		
		LogCustomProperty p = this.logCustomPropertyManager.getForUpdate(id) ;
		Assert.assertNotNull(p, "自定义属性不存在！") ;
		
		this.sessionManager.assertOwner(loginUser, Constants.serviceName.APP_LOG, String.valueOf(p.getAppId())) ;
		
		this.logCustomPropertyManager.delete(p) ;
		
		return new ModelAndView("/console/log/customPropertyList", "appId", p.getAppId());
	}

	public ISessionManager getSessionManager() {
		return sessionManager;
	}

	public void setSessionManager(ISessionManager sessionManager) {
		this.sessionManager = sessionManager;
	}

	public ILogCustomPropertyManager getLogCustomPropertyManager() {
		return logCustomPropertyManager;
	}

	public void setLogCustomPropertyManager(ILogCustomPropertyManager logCustomPropertyManager) {
		this.logCustomPropertyManager = logCustomPropertyManager;
	}

}
