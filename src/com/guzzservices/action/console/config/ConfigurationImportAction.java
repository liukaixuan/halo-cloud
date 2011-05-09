package com.guzzservices.action.console.config;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
 * 导入配置组
 * 
 * @author liu kaixuan
 */
public class ConfigurationImportAction implements Controller {
	
	private IConfigurationManager configurationManager ;
	
	private ISessionManager sessionManager ;
	
	private String formView ;
	
	private String successView ;
	
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		LoginUser loginUser = sessionManager.getLoginUser(request, response) ;
		String groupId = request.getParameter("groupId") ;
		
		//check permission
		this.sessionManager.assertOwner(loginUser, Constants.serviceName.CONFIGURATION, groupId) ;

		HashMap<String, String> model = new HashMap<String, String>() ;
		model.put("groupId", groupId) ;
		
		if("POST".equalsIgnoreCase(request.getMethod())){
			String json = request.getParameter("json") ;
			
			if(json != null){
				List<Map> params = JsonUtil.fromJson(json.trim(), LinkedList.class) ;
				
				for(Map<String, String> m : params){
					String parameter = (String) m.get("parameter") ;
					
					//已经存在了。
					if(this.configurationManager.getByParameterName(groupId, parameter)  != null){
						continue ;
					}
					
					Configuration c = new Configuration() ;
					
					c.setCreatedTime(new java.util.Date()) ;
					c.setGroupId(groupId) ;
					c.setName(m.get("name")) ;
					c.setParameter(parameter) ;
					c.setType(m.get("type")) ;
					c.setValue(m.get("value")) ;
					c.setValidValues(m.get("validValues")) ;
					c.setDescription(m.get("description")) ;
					
					//验证有效性
					c.assertValueValid(c.getValue()) ;
					
					this.configurationManager.save(c) ;
				}
			}
			
			return new ModelAndView(getSuccessView(), model);
		}else{
			return new ModelAndView(getFormView(), model);
		}
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

	public String getFormView() {
		return formView;
	}

	public void setFormView(String formView) {
		this.formView = formView;
	}
	
}
