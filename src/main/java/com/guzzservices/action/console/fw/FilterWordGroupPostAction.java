/**
 * FilterWordPostAction.java. created on 2006-7-13  
 */
package com.guzzservices.action.console.fw;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import com.guzzservices.action.vo.FilterWordGroupForm;
import com.guzzservices.business.FilterWordGroup;
import com.guzzservices.manager.Constants;
import com.guzzservices.manager.IFilterWordGroupManager;
import com.guzzservices.manager.ISessionManager;
import com.guzzservices.sso.LoginUser;

/**
 * 
 * delete a group.
 * 
 * @author liu kaixuan
 */
public class FilterWordGroupPostAction extends SimpleFormController {

	private IFilterWordGroupManager filterWordGroupManager ;
	
	private ISessionManager sessionManager ;
	
	public FilterWordGroupPostAction(){
		this.setCommandName("filterWordGroupForm") ;
	}
	
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		LoginUser loginUser = sessionManager.getLoginUser(request, response) ;
		
		FilterWordGroupForm form = (FilterWordGroupForm) command ;
		FilterWordGroup group = form.getFilterWordGroup() ;
		
		if(form.isNewGroup()){
			group.setUserId(loginUser.getUserId()) ;
			group.setCreatedTime(new Date()) ;
			
			this.filterWordGroupManager.add(loginUser, group) ;
		}else{
			//check for permissions
			this.sessionManager.assertOwner(loginUser, Constants.serviceName.FILTER_WORD, group.getId()) ;
			
			this.filterWordGroupManager.update(group) ;
		}
		
		return new ModelAndView(getSuccessView(), "groupId", group.getId()) ;
	}

	@Override
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		String groupId = request.getParameter("groupId") ;
		
		if(groupId == null) return new FilterWordGroupForm(null) ;
		
		FilterWordGroup group = this.filterWordGroupManager.getById(groupId) ;
		
		return new FilterWordGroupForm(group) ;
	}

	@Override
	protected void onBindAndValidate(HttpServletRequest request, Object command, BindException errors) throws Exception {
		super.onBindAndValidate(request, command, errors);
	}

	public IFilterWordGroupManager getFilterWordGroupManager() {
		return filterWordGroupManager;
	}

	public void setFilterWordGroupManager(IFilterWordGroupManager filterWordGroupManager) {
		this.filterWordGroupManager = filterWordGroupManager;
	}

	public ISessionManager getSessionManager() {
		return sessionManager;
	}

	public void setSessionManager(ISessionManager sessionManager) {
		this.sessionManager = sessionManager;
	}

}
