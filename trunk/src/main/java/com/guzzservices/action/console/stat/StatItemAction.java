package com.guzzservices.action.console.stat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.guzz.util.RequestUtil;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import com.guzzservices.action.vo.StatItemForm;
import com.guzzservices.business.StatItem;
import com.guzzservices.manager.Constants;
import com.guzzservices.manager.ISessionManager;
import com.guzzservices.manager.IStatItemManager;
import com.guzzservices.sso.LoginUser;

/**
 * 
 * 
 * @author liu kaixuan
 */
public class StatItemAction extends SimpleFormController {

	private IStatItemManager statItemManager ;
	
	private ISessionManager sessionManager ;
	
	public StatItemAction() {
		this.setCommandName("statItemForm") ;
	}
	
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		StatItemForm itemForm = (StatItemForm) command ;
		StatItem item = itemForm.getStatItem() ;
		if(itemForm.isNewStatItem()){
			statItemManager.insert(item) ;
		}else{
			statItemManager.update(item) ;
		}
		
		return new ModelAndView(getSuccessView(), "groupId", item.getGroupId()) ;
	}
	
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		LoginUser loginUser = sessionManager.getLoginUser(request, null) ;
		int id = RequestUtil.getParameterAsInt(request, "id", -1) ;
		String groupId = request.getParameter("groupId") ;
		
		StatItem item = null ;
		
		if(id > 0){
			item = statItemManager.getForUpdate(id) ;
		
			this.sessionManager.assertOwner(loginUser, Constants.serviceName.STAT_ITEM, String.valueOf(item.getGroupId())) ;
			
			return new StatItemForm(item) ;
		}else{
			this.sessionManager.assertOwner(loginUser, Constants.serviceName.STAT_ITEM, groupId) ;
			
			return new StatItemForm(Integer.parseInt(groupId), loginUser.getUserId()) ;
		}
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
