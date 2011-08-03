/**
 * FilterWordDeleteAction.java. created on 2006-7-13  
 */
package com.guzzservices.action.console.fw;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.guzz.util.Assert;
import org.guzz.util.RequestUtil;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.guzzservices.business.FilterWord;
import com.guzzservices.manager.Constants;
import com.guzzservices.manager.IFilterWordManager;
import com.guzzservices.manager.ISessionManager;
import com.guzzservices.sso.LoginUser;

/**
 * 删除一个过滤词
 * 
 * @author liu kaixuan
 * @date 2006-7-13 14:26:28
 */
public class FilterWordDeleteAction implements Controller {

	private IFilterWordManager filterWordManager ;
	
	private ISessionManager sessionManager ;
	
	private String successView ;
	
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		int id = RequestUtil.getParameterAsInt(request, "id", -1) ;
		LoginUser loginUser = sessionManager.getLoginUser(request, response) ;
		
		FilterWord word = filterWordManager.getById(id) ;
		Assert.assertNotNull(word, "FilterWord not exists.") ;
		
		//check permission
		this.sessionManager.assertCommiter(loginUser, Constants.serviceName.FILTER_WORD, word.getGroupId()) ;
		
		filterWordManager.remove(word) ;
		
		return new ModelAndView(getSuccessView(), "groupId", word.getGroupId());
	}

	public String getSuccessView() {
		return successView;
	}

	public void setSuccessView(String successView) {
		this.successView = successView;
	}

	public IFilterWordManager getFilterWordManager() {
		return filterWordManager;
	}

	public void setFilterWordManager(IFilterWordManager badWordManager) {
		this.filterWordManager = badWordManager;
	}

	public ISessionManager getSessionManager() {
		return sessionManager;
	}

	public void setSessionManager(ISessionManager sessionManager) {
		this.sessionManager = sessionManager;
	}

}
