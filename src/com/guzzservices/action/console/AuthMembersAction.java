package com.guzzservices.action.console;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.guzz.util.Assert;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import com.guzzservices.action.vo.AuthMembersForm;
import com.guzzservices.manager.IAuthManager;
import com.guzzservices.manager.ISessionManager;
import com.guzzservices.manager.IUserManager;
import com.guzzservices.manager.impl.Members;
import com.guzzservices.sso.LoginUser;
import com.guzzservices.util.ValidationUtil;

/**
 * 服务授权。
 * 
 * @author liu kaixuan
 */
public class AuthMembersAction extends SimpleFormController {
	
	private IAuthManager authManager ;
	
	private ISessionManager sessionManager ;
	
	private IUserManager userManager ;
		
	public AuthMembersAction() {
		this.setCommandName("membersForm") ;
	}

	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object cmd, BindException errors) throws Exception {
		LoginUser loginUser = sessionManager.getLoginUser(request, null) ;
		String serviceName = request.getParameter("serviceName") ;
		String serviceKey = request.getParameter("serviceKey") ;
		
		this.sessionManager.assertOwner(loginUser, serviceName, serviceKey) ;
		
		AuthMembersForm form = (AuthMembersForm) cmd ;
		
		Members m = Members.buildMembers(form.getOwners(), form.getCommiters()) ;
		this.authManager.storeAuthedMembers(serviceName, serviceKey, m) ;
		
		HashMap<String, String> params = new HashMap<String, String>() ;
		params.put("serviceName", serviceName) ;
		params.put("serviceKey", serviceKey) ;
		params.put("success", "1") ;
		
		return new ModelAndView(getSuccessView(), params) ;
	}

	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		LoginUser loginUser = sessionManager.getLoginUser(request, null) ;
		String serviceName = request.getParameter("serviceName") ;
		String serviceKey = request.getParameter("serviceKey") ;
		
		Assert.assertNotEmpty(serviceName, "missing parameter serviceName") ;
		Assert.assertNotEmpty(serviceKey, "missing parameter serviceKey") ;

		//
		this.sessionManager.assertCommiter(loginUser, serviceName, serviceKey) ;
		
		boolean isOwner = this.sessionManager.isOwner(loginUser, serviceName, serviceKey) ;
		request.setAttribute("isOwner", isOwner) ;
		
		Members m = this.authManager.getAuthedMembers(serviceName, serviceKey) ;
		AuthMembersForm form = new AuthMembersForm(m == null ? null : m.getOwnersString(), m == null ? null : m.getCommitersString()) ;
		
		return form ;
	}
	
	protected void onBindAndValidate(HttpServletRequest request, Object command, BindException errors) throws Exception {
		super.onBindAndValidate(request, command, errors);
				
		AuthMembersForm form = (AuthMembersForm) command ;
			
		Members m = Members.buildMembers(form.getOwners(), form.getCommiters()) ;
		if(!m.hasOwner()){
			ValidationUtil.reject(errors, "owners", null, "至少指定1名拥有者!") ;
		}
		
		//check user exist.
		if(m.getOwners().size() > 100){
			ValidationUtil.reject(errors, "owners", null, "拥有者过多，最多100人!") ;
		}else{
			for(String userName : m.getOwners()){
				if(userManager.getByEmail(userName) == null){
					ValidationUtil.reject(errors, "owners", null, "用户[" + userName + "]不存在!") ;
				}
			}
		}
		
		if(m.getCommiters().size() > 100){
			ValidationUtil.reject(errors, "owners", null, "贡献者过多，最多100人!") ;
		}else{
			for(String userName : m.getCommiters()){
				if(userManager.getByEmail(userName) == null){
					ValidationUtil.reject(errors, "commiters", null, "用户[" + userName + "]不存在!") ;
				}
			}
		}
	}

	public ISessionManager getSessionManager() {
		return sessionManager;
	}

	public void setSessionManager(ISessionManager sessionManager) {
		this.sessionManager = sessionManager;
	}

	public IAuthManager getAuthManager() {
		return authManager;
	}

	public void setAuthManager(IAuthManager authManager) {
		this.authManager = authManager;
	}

	public IUserManager getUserManager() {
		return userManager;
	}

	public void setUserManager(IUserManager userManager) {
		this.userManager = userManager;
	}

}
