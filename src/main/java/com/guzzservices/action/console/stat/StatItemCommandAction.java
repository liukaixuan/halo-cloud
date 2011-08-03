package com.guzzservices.action.console.stat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.guzz.exception.IllegalParameterException;
import org.guzz.util.Assert;
import org.guzz.util.RequestUtil;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.guzzservices.business.StatItem;
import com.guzzservices.manager.Constants;
import com.guzzservices.manager.ISessionManager;
import com.guzzservices.manager.IStatItemManager;
import com.guzzservices.manager.ITopRecordManager;
import com.guzzservices.sso.LoginUser;
import com.guzzservices.util.AjaxUtil;

/**
 * 对StatItem的一些操作。
 * 
 * @author liu kaixuan
 */
public class StatItemCommandAction implements Controller {
	private ITopRecordManager topRecordManager ;
	private IStatItemManager statItemManager ;
	private ISessionManager sessionManager ;
	private String successView ;
		
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		LoginUser loginUser = sessionManager.getLoginUser(request, response) ;
		String action = request.getParameter("action") ;
		int statId = RequestUtil.getParameterAsInt(request, "statId", -1) ;		
		StatItem item = statItemManager.getForRead(statId) ;
		Assert.assertResouceNotNull(item, "要统计编号为:[" + statId + "]的项目不存在！") ;
		
		this.sessionManager.assertCommiter(loginUser, Constants.serviceName.STAT_ITEM, String.valueOf(item.getGroupId())) ;
				
		if("sort".equalsIgnoreCase(action)){
			topRecordManager.sortRecords(item, false) ;
		}else if("refresh".equalsIgnoreCase(action)){			
			statItemManager.refreshRecords(item) ;
		}else if("publish".equalsIgnoreCase(action)){
			String result = statItemManager.publishRecords(item) ;
			
			AjaxUtil.sendText(request, response, result) ;
			return null ;
		}else{
			throw new IllegalParameterException("未知操作：[" + action + "]，请确定来自正确的链接地址.") ;
		}
		
		return new ModelAndView(getSuccessView(), "statId", String.valueOf(statId)) ;
	}

	public IStatItemManager getStatItemManager() {
		return statItemManager;
	}

	public void setStatItemManager(IStatItemManager statItemManager) {
		this.statItemManager = statItemManager;
	}

	public String getSuccessView() {
		return successView;
	}

	public void setSuccessView(String successView) {
		this.successView = successView;
	}

	public ITopRecordManager getTopRecordManager() {
		return topRecordManager;
	}

	public void setTopRecordManager(ITopRecordManager topRecordManager) {
		this.topRecordManager = topRecordManager;
	}

	public ISessionManager getSessionManager() {
		return sessionManager;
	}

	public void setSessionManager(ISessionManager sessionManager) {
		this.sessionManager = sessionManager;
	}

}
