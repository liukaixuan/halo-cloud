/**
 * 
 */
package com.guzzservices.action.console.log;

import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.guzz.dao.PageFlip;
import org.guzz.util.DateUtil;
import org.guzz.util.RequestUtil;
import org.guzz.util.StringUtil;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.guzzservices.management.AppLogService;
import com.guzzservices.manager.Constants;
import com.guzzservices.manager.ILogAppManager;
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
	
	private ILogAppManager logAppManager ;

	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		LoginUser loginUser = sessionManager.getLoginUser(request, response) ;
		String appId = request.getParameter("appId") ;
		
		this.sessionManager.assertOwner(loginUser, Constants.serviceName.APP_LOG, appId) ;
		
		String scode = this.logAppManager.getForRead(Integer.parseInt(appId)).getSecureCode() ;
		
		//yyyy-MM-dd HH:mm:ss
		String startTime = request.getParameter("startTime") ;
		String endTime = request.getParameter("endTime") ;
		int userId = RequestUtil.getParameterAsInt(request, "userId", -1) ;
		int pageNo = RequestUtil.getParameterAsInt(request, "pageNo", 1) ;
		
		//如果没有条件，查找本人今天的记录。方便用户了解参数格式。
		if(userId < 1 && StringUtil.isEmpty(startTime) && StringUtil.isEmpty(endTime)){
			userId = loginUser.getUserId() ;
			
			Calendar cal = Calendar.getInstance() ;
			cal.set(Calendar.HOUR_OF_DAY, 0) ;
			cal.set(Calendar.MINUTE, 0) ;
			cal.set(Calendar.SECOND, 0) ;
			cal.set(Calendar.MILLISECOND, 0) ;
			
			startTime = DateUtil.date2String(cal.getTime(), "yyyy-MM-dd HH:mm:ss") ;
		}

		HashMap<String, Object> params = new HashMap<String, Object>() ;
		
		LinkedList<String> conditions = new LinkedList<String>() ;
		if(userId > 0){
			conditions.addLast("userId=" + userId) ;
			params.put("userId", userId) ;
		}
		
		if(StringUtil.notEmpty(startTime)){
			conditions.addLast("createdTime>=" + startTime) ;
			params.put("startTime", startTime) ;
		}
		
		if(StringUtil.notEmpty(endTime)){
			conditions.addLast("createdTime<=" + endTime) ;
			params.put("endTime", endTime) ;
		}
		
		params.put("appId", appId) ;
		
		PageFlip logs = this.appLogService.queryLogs(scode, conditions, "id asc", pageNo, 20) ;
		
		if(logs != null){
			logs.setFlipURL(request, "pageNo") ;
			params.put("logs", logs) ;
		}
		
		Map<String, String> customProperties = this.appLogService.queryCustomPropsMetaInfo(scode) ;
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

	public ILogAppManager getLogAppManager() {
		return logAppManager;
	}

	public void setLogAppManager(ILogAppManager logAppManager) {
		this.logAppManager = logAppManager;
	}

}
