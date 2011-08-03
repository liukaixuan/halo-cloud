/**
 * 
 */
package com.guzzservices.action.console.stat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.guzz.util.Assert;
import org.guzz.util.RequestUtil;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.guzzservices.business.StatItem;
import com.guzzservices.manager.Constants;
import com.guzzservices.manager.ISessionManager;
import com.guzzservices.manager.IStatItemManager;
import com.guzzservices.sso.LoginUser;

/**
 * 
 * 
 * 
 * @author liukaixuan(liukaixuan@gmail.com)
 */
public class StatItemRescheduleAction implements Controller {
	
	private IStatItemManager statItemManager ;
	
	private ISessionManager sessionManager ;

	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		LoginUser loginUser = sessionManager.getLoginUser(request, response) ;
		int id = RequestUtil.getParameterAsInt(request, "id", 0) ;
		
		String newCron = request.getParameter("cron") ;
		Assert.assertNotEmpty(newCron, "new cron required.") ;
		
		StatItem statItem = statItemManager.getForUpdate(id) ;
		if(statItem != null){
			this.sessionManager.assertOwner(loginUser, Constants.serviceName.STAT_ITEM, String.valueOf(statItem.getGroupId())) ;
			
			this.statItemManager.reScheduleTask(statItem, newCron) ;
		}
		
		return null;
	}

	public IStatItemManager getStatItemManager() {
		return statItemManager;
	}

	public void setStatItemManager(IStatItemManager statItemManager) {
		this.statItemManager = statItemManager;
	}

	public ISessionManager getSessionManager() {
		return sessionManager;
	}

	public void setSessionManager(ISessionManager sessionManager) {
		this.sessionManager = sessionManager;
	}

}
