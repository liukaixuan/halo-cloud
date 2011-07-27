/**
 * 
 */
package com.guzzservices.action.console.log;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.guzz.dao.PageFlip;
import org.guzz.util.RequestUtil;
import org.guzz.util.StringUtil;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.guzzservices.management.AppLogService;
import com.guzzservices.manager.Constants;
import com.guzzservices.manager.ISessionManager;
import com.guzzservices.sso.LoginUser;

/**
 * 
 * 日志记录列表。
 * 
 * @author liukaixuan(liukaixuan@gmail.com)
 */
public class LogRecordListAction implements Controller {
	
	private ISessionManager sessionManager ;
	
	private AppLogService appLogService ;

	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		LoginUser loginUser = sessionManager.getLoginUser(request, response) ;
		String appId = request.getParameter("appId") ;
		
		this.sessionManager.assertOwner(loginUser, Constants.serviceName.APP_LOG, appId) ;
		
		//yyyy-MM-dd HH:mm:ss
		String startTime = request.getParameter("startTime") ;
		String endTime = request.getParameter("endTime") ;
		int userId = RequestUtil.getParameterAsInt(request, "userId", -1) ;
		int pageNo = RequestUtil.getParameterAsInt(request, "pageNo", 1) ;
		
		LinkedList<String> conditions = new LinkedList<String>() ;
		if(userId > 0){
			conditions.addLast("userId=" + userId) ;
		}
		
		if(StringUtil.notEmpty(startTime)){
			conditions.addLast("createdTime>=" + startTime) ;
		}
		
		if(StringUtil.notEmpty(endTime)){
			conditions.addLast("createdTime<=" + endTime) ;
		}
		
		HashMap<String, Object> params = new HashMap<String, Object>() ;
		params.put("appId", appId) ;
		
		PageFlip logs = this.appLogService.queryLogs(conditions, "id asc", pageNo, 20) ;
		
		if(logs != null){
			logs.setFlipURL(request, "pageNo") ;
			params.put("logs", logs) ;
		}
		
		Map<String, String> customProperties = this.appLogService.queryCustomPropsMetaInfo() ;
		Set<String> customPropNames = customProperties.keySet() ;
		
		params.put("customProperties", customProperties) ;
		params.put("customPropNames", customPropNames) ;
		
		return new ModelAndView("/console/log/logRecordList", params);
	}

	public ISessionManager getSessionManager() {
		return sessionManager;
	}

	public void setSessionManager(ISessionManager sessionManager) {
		this.sessionManager = sessionManager;
	}

	public AppLogService getAppLogService() {
		return appLogService;
	}

	public void setAppLogService(AppLogService appLogService) {
		this.appLogService = appLogService;
	}

}
