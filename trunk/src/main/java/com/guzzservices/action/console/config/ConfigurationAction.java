package com.guzzservices.action.console.config;

import java.util.Date;
import java.util.LinkedList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.guzz.util.StringUtil;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import com.guzzservices.action.vo.ConfigurationForm;
import com.guzzservices.business.Configuration;
import com.guzzservices.manager.Constants;
import com.guzzservices.manager.IConfigurationManager;
import com.guzzservices.manager.ISessionManager;
import com.guzzservices.sso.LoginUser;
import com.guzzservices.util.ValidationUtil;

/**
 * 添加修改一个系统配置。
 * 
 * @author liu kaixuan
 */
public class ConfigurationAction extends SimpleFormController {
	
	private IConfigurationManager configurationManager ;
	
	private ISessionManager sessionManager ;
	
	LinkedList<String> dataTypes = new LinkedList<String>() ;
	{
		dataTypes.addLast("string") ;
		dataTypes.addLast("boolean") ;
		dataTypes.addLast("int") ;
		dataTypes.addLast("float") ;
		dataTypes.addLast("double") ;
		dataTypes.addLast("text") ;
	}
	
	public ConfigurationAction() {
		super();
		this.setCommandName("configForm") ;
	}

	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object cmd, BindException errors) throws Exception {
		ConfigurationForm form = (ConfigurationForm) cmd ;
		Configuration config = form.getConfiguration() ;
		
		if(form.isNewConfig()){
			config.setCreatedTime(new Date()) ;
			this.configurationManager.save(config) ;
		}else{
			this.configurationManager.update(config) ;
		}
		
		return new ModelAndView(getSuccessView(), "groupId", config.getGroupId()) ;
	}

	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		LoginUser loginUser = sessionManager.getLoginUser(request, null) ;
		String id = request.getParameter("id") ;
		String groupId = request.getParameter("groupId") ;
		
		//check permission
		this.sessionManager.assertCommiter(loginUser, Constants.serviceName.CONFIGURATION, groupId) ;
		
		request.setAttribute("dataTypes", dataTypes) ;
		
		if(StringUtil.notEmpty(id)){
			return new ConfigurationForm(this.configurationManager.getById(id), groupId) ;
		}else{
			return new ConfigurationForm(null, groupId) ;
		}
	}
	
	protected void onBindAndValidate(HttpServletRequest request, Object command, BindException errors) throws Exception {
		super.onBindAndValidate(request, command, errors);
		
		ValidationUtil.rejectIfEmptyOrWhitespace(errors, "configuration.parameter", null, "参数名不能为空!");
		ValidationUtil.rejectIfLongThan(errors, "configuration.parameter", "configuration.validator.parameter.toolong", "参数名太长，最多64个字!", 64) ;
		
		try{
			Configuration c = ((ConfigurationForm) command ).getConfiguration() ;
			c.assertValueValid(c.getValue()) ;
		}catch(Exception e){
			ValidationUtil.reject(errors, "configuration.value", null, e.getMessage()) ;
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

}
