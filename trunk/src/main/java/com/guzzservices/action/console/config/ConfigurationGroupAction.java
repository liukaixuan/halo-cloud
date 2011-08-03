package com.guzzservices.action.console.config;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.guzz.util.StringUtil;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import com.guzzservices.action.vo.ConfigurationGroupForm;
import com.guzzservices.business.ConfigurationGroup;
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
public class ConfigurationGroupAction extends SimpleFormController {
	
	private IConfigurationManager configurationManager ;
	
	private ISessionManager sessionManager ;
	
	public ConfigurationGroupAction() {
		super();
		this.setCommandName("configGroupForm") ;
	}

	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object cmd, BindException errors) throws Exception {
		LoginUser loginUser = sessionManager.getLoginUser(request, response) ;

		ConfigurationGroupForm form = (ConfigurationGroupForm) cmd ;
		ConfigurationGroup group = form.getConfigurationGroup() ;
		
		if(form.isNewGroup()){
			group.setCreatedTime(new Date()) ;
			group.setUserId(loginUser.getUserId()) ;
			
			this.configurationManager.addGroup(loginUser, group) ;
		}else{
			//check for permissions
			this.sessionManager.assertOwner(loginUser, Constants.serviceName.CONFIGURATION, group.getId()) ;
			
			this.configurationManager.updateGroup(group) ;
		}
		
		return new ModelAndView(getSuccessView()) ;
	}

	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		String groupId = request.getParameter("groupId") ;
		
		if(StringUtil.isEmpty(groupId)){
			return new ConfigurationGroupForm(null) ;
		}else{
			return new ConfigurationGroupForm(this.configurationManager.getGroup(groupId)) ;
		}
	}

	protected void onBindAndValidate(HttpServletRequest request, Object command, BindException errors) throws Exception {
		super.onBindAndValidate(request, command, errors);
		
		ValidationUtil.rejectIfEmptyOrWhitespace(errors, "configurationGroup.name", null, "组名不能为空!");
		ValidationUtil.rejectIfLongThan(errors, "configurationGroup.name", "configuration.validator.name.toolong", "名称太长，最多64个字!", 64) ;
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
