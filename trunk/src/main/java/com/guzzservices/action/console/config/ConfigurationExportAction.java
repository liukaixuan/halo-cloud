package com.guzzservices.action.console.config;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.guzzservices.business.Configuration;
import com.guzzservices.manager.Constants;
import com.guzzservices.manager.IConfigurationManager;
import com.guzzservices.manager.ISessionManager;
import com.guzzservices.rpc.util.JsonUtil;
import com.guzzservices.sso.LoginUser;


/**
 * 导出配置组
 * 
 * @author liu kaixuan
 */
public class ConfigurationExportAction implements Controller {
	
	private IConfigurationManager configurationManager ;
	
	private ISessionManager sessionManager ;
	
	private String successView ;
	
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		LoginUser loginUser = sessionManager.getLoginUser(request, response) ;
		String groupId = request.getParameter("groupId") ;
		
		//check permission
		this.sessionManager.assertOwner(loginUser, Constants.serviceName.CONFIGURATION, groupId) ;
		
		List<Configuration> configs = configurationManager.listConfigurations(groupId) ;
		String json = JsonUtil.toJson(configs) ;
		
		HashMap<String, String> model = new HashMap<String, String>() ;
		model.put("groupId", groupId) ;
		model.put("json", json) ;
		
		return new ModelAndView(getSuccessView(), model);
	}

	public IConfigurationManager getConfigurationManager() {
		return configurationManager;
	}

	public void setConfigurationManager(IConfigurationManager configurationManager) {
		this.configurationManager = configurationManager;
	}

	public ISessionManager getSessionManager() {
		return sessionManager;
	}

	public void setSessionManager(ISessionManager sessionManager) {
		this.sessionManager = sessionManager;
	}

	public String getSuccessView() {
		return successView;
	}

	public void setSuccessView(String successView) {
		this.successView = successView;
	}
	
}
