package com.guzzservices.action.console.log;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.guzz.util.RequestUtil;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import com.guzzservices.action.vo.LogCustomPropertyForm;
import com.guzzservices.business.LogCustomProperty;
import com.guzzservices.manager.Constants;
import com.guzzservices.manager.ILogCustomPropertyManager;
import com.guzzservices.manager.ISessionManager;
import com.guzzservices.sso.LoginUser;
import com.guzzservices.util.ValidationUtil;

/**
 * 
 * 
 * @author liu kaixuan
 */
public class LogCustomPropertyAction extends SimpleFormController {

	private ILogCustomPropertyManager logCustomPropertyManager ;
	
	private ISessionManager sessionManager ;
	
	public LogCustomPropertyAction() {
		this.setCommandName("logCustomPropertyForm") ;
	}
	
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		LogCustomPropertyForm itemForm = (LogCustomPropertyForm) command ;
		LogCustomProperty item = itemForm.getProperty() ;
		
		if(itemForm.isNewProperty()){
			item.setCreatedTime(new Date()) ;
			logCustomPropertyManager.insert(item) ;
		}else{
			logCustomPropertyManager.update(item) ;
		}
		
		return new ModelAndView(getSuccessView(), "appId", item.getAppId()) ;
	}
	
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		LoginUser loginUser = sessionManager.getLoginUser(request, null) ;
		int id = RequestUtil.getParameterAsInt(request, "id", -1) ;
		String appId = request.getParameter("appId") ;
		
		LogCustomProperty m_logCustomProperty = null ;
		
		if(id > 0){
			m_logCustomProperty = logCustomPropertyManager.getForUpdate(id) ;
			this.sessionManager.assertOwner(loginUser, Constants.serviceName.APP_LOG, String.valueOf(m_logCustomProperty.getAppId())) ;
			
			return new LogCustomPropertyForm(m_logCustomProperty) ;
		}else{
			this.sessionManager.assertOwner(loginUser, Constants.serviceName.APP_LOG, appId) ;
			
			return new LogCustomPropertyForm(Integer.parseInt(appId)) ;
		}
	}
	
	protected void onBindAndValidate(HttpServletRequest request, Object command, BindException errors) throws Exception {
		super.onBindAndValidate(request, command, errors);
		
		ValidationUtil.rejectIfEmptyOrWhitespace(errors, "property.propName", null, "propName不能为空!");
		ValidationUtil.rejectIfEmptyOrWhitespace(errors, "property.colName", null, "colName不能为空!");
		ValidationUtil.rejectIfEmptyOrWhitespace(errors, "property.displayName", null, "displayName不能为空!");
		ValidationUtil.rejectIfEmptyOrWhitespace(errors, "property.dataType", null, "dataType不能为空!");
	}

	public ILogCustomPropertyManager getLogCustomPropertyManager() {
		return logCustomPropertyManager;
	}

	public void setLogCustomPropertyManager(ILogCustomPropertyManager logCustomPropertyManager) {
		this.logCustomPropertyManager = logCustomPropertyManager;
	}

	public ISessionManager getSessionManager() {
		return sessionManager;
	}

	public void setSessionManager(ISessionManager sessionManager) {
		this.sessionManager = sessionManager;
	}

}
