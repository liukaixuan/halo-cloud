/**
 * FilterWordPostAction.java. created on 2006-7-13  
 */
package com.guzzservices.action.console.fw;

import java.util.Date;

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
 * 添加/修改过滤词
 * 
 * @author liu kaixuan
 */
public class FilterWordPostAction implements Controller {

	private IFilterWordManager filterWordManager ;
		
	private ISessionManager sessionManager ;
	
	private String successView ;
	
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		int id = RequestUtil.getParameterAsInt(request, "id", -1) ;
		String groupId = request.getParameter("groupId") ;
		
		String word = request.getParameter("word") ;
		int level = RequestUtil.getParameterAsInt(request, "level", 1) ;
		FilterWord fw = null ;
		
		if(id > 0){
			fw = filterWordManager.getById(id) ;
			Assert.assertNotNull(fw, "FilterWord not exists.") ;
			groupId = fw.getGroupId() ;
		}
		
		//check for permissions
		LoginUser loginUser = sessionManager.getLoginUser(request, response) ;
		this.sessionManager.assertCommiter(loginUser, Constants.serviceName.FILTER_WORD, groupId) ;
		
		if(id > 0){//update
			fw.setLevel(level) ;
			
			this.filterWordManager.update(fw) ;
		}else{//add new
			fw = new FilterWord() ;
			fw.setGroupId(groupId) ;
			fw.setCreatedTime(new Date()) ;
			fw.setLevel(level) ;
			fw.setWord(word) ;
			
			this.filterWordManager.add(fw) ;
		}
		
		return new ModelAndView(getSuccessView(), "groupId", groupId) ;
	}

	public IFilterWordManager getFilterWordManager() {
		return filterWordManager;
	}

	public void setFilterWordManager(IFilterWordManager filterWordManager) {
		this.filterWordManager = filterWordManager;
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
