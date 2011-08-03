/**
 * FilterWordPostAction.java. created on 2006-7-13  
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
 * 
 * 
 * @author liu kaixuan
 */
public class AddFilterWordAction implements Controller {
	
	private ISessionManager sessionManager ;
	
	private IFilterWordManager filterWordManager ;

	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String groupId = request.getParameter("groupId") ;
		int id = RequestUtil.getParameterAsInt(request, "id") ;
		LoginUser loginUser = sessionManager.getLoginUser(request, response) ;
		
		if(id > 0){
			FilterWord fw = this.filterWordManager.getById(id) ;
			Assert.assertNotNull(fw, "过滤词不存在！") ;
			
			groupId = fw.getGroupId() ;
		}
		
		sessionManager.assertCommiter(loginUser, Constants.serviceName.FILTER_WORD, groupId) ;
		
		return new ModelAndView("/console/filterWord/addFilterWord", "groupId", groupId);
	}

	public ISessionManager getSessionManager() {
		return sessionManager;
	}

	public void setSessionManager(ISessionManager sessionManager) {
		this.sessionManager = sessionManager;
	}

	public IFilterWordManager getFilterWordManager() {
		return filterWordManager;
	}

	public void setFilterWordManager(IFilterWordManager filterWordManager) {
		this.filterWordManager = filterWordManager;
	}

}
