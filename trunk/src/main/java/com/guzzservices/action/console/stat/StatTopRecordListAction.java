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
public class StatTopRecordListAction implements Controller {
	
	private ISessionManager sessionManager ;
	
	private IStatItemManager statItemManager ;

	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		LoginUser loginUser = sessionManager.getLoginUser(request, response) ;
		int statId = RequestUtil.getParameterAsInt(request, "statId") ;
		
		StatItem si = this.statItemManager.getForRead(statId) ;
		Assert.assertNotNull(si, "统计项不存在！") ;
				
		this.sessionManager.assertCommiter(loginUser, Constants.serviceName.STAT_ITEM, String.valueOf(si.getGroupId())) ;
		
		return new ModelAndView("/console/stat/statTopRecordList", "statItem", si);
	}

	public ISessionManager getSessionManager() {
		return sessionManager;
	}

	public void setSessionManager(ISessionManager sessionManager) {
		this.sessionManager = sessionManager;
	}

	public IStatItemManager getStatItemManager() {
		return statItemManager;
	}

	public void setStatItemManager(IStatItemManager statItemManager) {
		this.statItemManager = statItemManager;
	}

}
