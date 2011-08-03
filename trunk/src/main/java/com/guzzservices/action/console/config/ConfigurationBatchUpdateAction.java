package com.guzzservices.action.console.config;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.guzz.util.Assert;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.guzzservices.business.Configuration;
import com.guzzservices.manager.Constants;
import com.guzzservices.manager.IConfigurationManager;
import com.guzzservices.manager.ISessionManager;
import com.guzzservices.sso.LoginUser;


/**
 * 批量更改系统配置信息。
 * 参数按照"config.parameter名=新的值"的方式提交，一次可能提交多个。
 * 
 * @author liu kaixuan
 */
public class ConfigurationBatchUpdateAction implements Controller {
	
	private IConfigurationManager configurationManager ;
	
	private ISessionManager sessionManager ;
	
	private String successView ;
	
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		LoginUser loginUser = sessionManager.getLoginUser(request, response) ;
		String groupId = request.getParameter("groupId") ;
		
		//check permission
		this.sessionManager.assertCommiter(loginUser, Constants.serviceName.CONFIGURATION, groupId) ;
		
		//读取所有提供的所有参数。
		Map<String, Object> params = request.getParameterMap() ;
		List<Configuration> configs = new LinkedList<Configuration>() ;
		
		Iterator<String> paramKeys = params.keySet().iterator() ;
		
		while(paramKeys.hasNext()){
			String param = paramKeys.next() ;
			
			if(!param.startsWith("config.")) continue ;
			
			String value = request.getParameter(param) ;
			param = param.substring("config.".length()) ;
			
			Configuration config = this.configurationManager.getByParameterName(groupId, param) ;
			
			Assert.assertResouceNotNull(config, "unknown config:" + param) ;
			
			//验证有效性
			config.assertValueValid(value) ;
			
			//passed。
			config.setValue(value) ;
			configs.add(config) ;			
		}
		
		//所有提交的内容都没有错误，update到数据库。
		this.configurationManager.updateByBatch(groupId, configs) ;
		
		HashMap<String, String> model = new HashMap<String, String>() ;
		model.put("groupId", groupId) ;
		
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
