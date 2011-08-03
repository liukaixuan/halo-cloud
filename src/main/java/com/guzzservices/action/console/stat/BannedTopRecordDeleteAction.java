package com.guzzservices.action.console.stat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.guzz.util.RequestUtil;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.guzzservices.business.BannedTopRecord;
import com.guzzservices.manager.Constants;
import com.guzzservices.manager.IBannedTopRecordManager;
import com.guzzservices.manager.ISessionManager;
import com.guzzservices.sso.LoginUser;
import com.guzzservices.util.AjaxUtil;

/**
 * 
 * @author liu kaixuan
 */
public class BannedTopRecordDeleteAction implements Controller {
	
	private IBannedTopRecordManager bannedTopRecordManager ;
	
	private ISessionManager sessionManager ;
	
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		LoginUser loginUser = sessionManager.getLoginUser(request, response) ;
		int id = RequestUtil.getParameterAsInt(request, "id") ;
		
		String msg = null ;
		
		BannedTopRecord btr = this.bannedTopRecordManager.getForUpdate(id) ;
		if(btr == null){
			msg = "记录不存在！ " ;
		}else if(!this.sessionManager.isCommiter(loginUser, Constants.serviceName.STAT_ITEM, String.valueOf(btr.getGroupId()))){
			msg = "没有权限！" ;
		}else{
			this.bannedTopRecordManager.delete(btr) ;
			msg = "记录：[" + btr.getObjectId() + "成功删除！]" ;
		}
		
		AjaxUtil.sendHtml(request, response, msg) ;
		
		return null ;
	}

	public ISessionManager getSessionManager() {
		return sessionManager;
	}

	public void setSessionManager(ISessionManager sessionManager) {
		this.sessionManager = sessionManager;
	}

	public IBannedTopRecordManager getBannedTopRecordManager() {
		return bannedTopRecordManager;
	}

	public void setBannedTopRecordManager(IBannedTopRecordManager bannedTopRecordManager) {
		this.bannedTopRecordManager = bannedTopRecordManager;
	}

}
