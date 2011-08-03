/**
 * 
 */
package com.guzzservices.action.console.stat;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.guzz.util.RequestUtil;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.guzzservices.business.StatItemGroup;
import com.guzzservices.manager.Constants;
import com.guzzservices.manager.ISessionManager;
import com.guzzservices.manager.IStatItemGroupManager;
import com.guzzservices.sso.LoginUser;

/**
 * 
 * 
 * 
 * @author liukaixuan(liukaixuan@gmail.com)
 */
public class StatItemGroupAction implements Controller {
	
	private IStatItemGroupManager statItemGroupManager ;
	
	private ISessionManager sessionManager ;

	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		LoginUser loginUser = sessionManager.getLoginUser(request, response) ;
		int groupId = RequestUtil.getParameterAsInt(request, "groupId", 0) ;
		String groupName = request.getParameter("groupName") ;
		
		if(groupId > 0){
			this.sessionManager.assertOwner(loginUser, Constants.serviceName.STAT_ITEM, String.valueOf(groupId)) ;
			
			//edit
			StatItemGroup sig = this.statItemGroupManager.getForUpdate(groupId) ;
			
			if(sig == null){
				return null ;
			}
			
			sig.setName(groupName) ;
			
			this.statItemGroupManager.update(sig) ;
		}else{
			StatItemGroup sig = new StatItemGroup() ;
			sig.setCreatedTime(new Date()) ;
			sig.setName(groupName) ;
			sig.setUserId(loginUser.getUserId()) ;
			
			this.statItemGroupManager.add(loginUser, sig) ;
		}
		
		return null;
	}

	public IStatItemGroupManager getStatItemGroupManager() {
		return statItemGroupManager;
	}

	public void setStatItemGroupManager(IStatItemGroupManager statItemGroupManager) {
		this.statItemGroupManager = statItemGroupManager;
	}

	public ISessionManager getSessionManager() {
		return sessionManager;
	}

	public void setSessionManager(ISessionManager sessionManager) {
		this.sessionManager = sessionManager;
	}

}
