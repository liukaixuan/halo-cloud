package com.guzzservices.action.console.stat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.guzz.exception.IllegalParameterException;
import org.guzz.util.Assert;
import org.guzz.util.RequestUtil;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.guzzservices.business.StatItem;
import com.guzzservices.business.TopRecord;
import com.guzzservices.manager.Constants;
import com.guzzservices.manager.ISessionManager;
import com.guzzservices.manager.IStatItemManager;
import com.guzzservices.manager.ITopRecordManager;
import com.guzzservices.sso.LoginUser;

/**
 * 控制Top排行记录的可见性。
 *  
 * @author liu kaixuan
 */
public class TopRecordVisibilityAction implements Controller {
	
	private ITopRecordManager topRecordManager ;
	
	private IStatItemManager statItemManager ;
	
	private ISessionManager sessionManager ;
	
	private String successView ;
	
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		LoginUser loginUser = sessionManager.getLoginUser(request, response) ;
		String action = request.getParameter("action") ;
		int id = RequestUtil.getParameterAsInt(request, "id", -1) ;
		int statId = RequestUtil.getParameterAsInt(request, "statId", -1) ;
		int[] ids = null ;
		
		if(id > 0){
			ids = new int[]{id} ;
		}else{
			ids = RequestUtil.getParameterAsIntArray(request, "ids", -1) ;
		}
		
		for(int i = 0 ; i < ids.length ; i++){
			if(ids[i] <= 0) continue ;
			
			TopRecord record = topRecordManager.getForUpdate(ids[i]) ;
			Assert.assertResouceNotNull(record, "记录不存在！") ;
			
			StatItem item = statItemManager.getForRead(record.getStatId()) ;
			Assert.assertResouceNotNull(item, "统计项不存在！") ;
			
			this.sessionManager.assertCommiter(loginUser, Constants.serviceName.STAT_ITEM, String.valueOf(item.getGroupId())) ;
			
			if("hide".equalsIgnoreCase(action)){
				topRecordManager.hideRecord(record) ;
			}else if("show".equalsIgnoreCase(action)){
				topRecordManager.showRecord(record) ;
			}else{
				throw new IllegalParameterException("未知操作：[" + action + "]，请确定来自正确的链接地址.") ;
			}
		}
		
		return new ModelAndView(getSuccessView(), "statId", statId) ;
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

	public IStatItemManager getStatItemManager() {
		return statItemManager;
	}

	public void setStatItemManager(IStatItemManager statItemManager) {
		this.statItemManager = statItemManager;
	}

}
