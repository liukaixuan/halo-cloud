package com.guzzservices.action.console.stat;

import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.guzz.util.Assert;
import org.guzz.util.DateUtil;
import org.guzz.util.RequestUtil;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import com.guzzservices.action.vo.TopRecordForm;
import com.guzzservices.business.StatItem;
import com.guzzservices.business.TopRecord;
import com.guzzservices.manager.Constants;
import com.guzzservices.manager.ISessionManager;
import com.guzzservices.manager.IStatItemManager;
import com.guzzservices.manager.ITopRecordManager;
import com.guzzservices.sso.LoginUser;

/**
 * 
 * 
 * @author liu kaixuan
 */
public class TopRecordAction extends SimpleFormController {

	private ITopRecordManager topRecordManager ;
	
	private IStatItemManager statItemManager ;
	
	private ISessionManager sessionManager ;
	
	public TopRecordAction() {
		this.setCommandName("topRecordForm") ;
	}

	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object cmd, BindException errors) throws Exception {
		int statId = RequestUtil.getParameterAsInt(request, "statId", -1) ;
		
		StatItem item = statItemManager.getForRead(statId) ;
		Assert.assertResouceNotNull(item, "统计项不存在！") ;
		Assert.assertResouceNotNull(item, "统计项不存在！") ;
		
		TopRecordForm form = (TopRecordForm) cmd ;
		TopRecord record = form.getTopRecord() ;
		
		if(form.isNewTopRecord()){
			topRecordManager.insert(record) ;
		}else{
			topRecordManager.update(record) ;
		}
		
		HashMap model = new HashMap() ;
		model.put("statId", String.valueOf(record.getStatId())) ;
		
		return new ModelAndView(getSuccessView(), model) ;
	}

	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		int id = RequestUtil.getParameterAsInt(request, "id", -1) ;
		int statId = RequestUtil.getParameterAsInt(request, "statId", -1) ;
		LoginUser loginUser = sessionManager.getLoginUser(request, null) ;
		
		StatItem item = null ;
		TopRecord record = null ;
		
		if(id > 0){
			record = topRecordManager.getForUpdate(id) ;
			Assert.assertResouceNotNull(record, "要修改的临时记录不存在！") ;
			item = statItemManager.getForRead(record.getStatId()) ;
			Assert.assertResouceNotNull(item, "统计项不存在！") ;
			
			this.sessionManager.assertCommiter(loginUser, Constants.serviceName.STAT_ITEM, String.valueOf(item.getGroupId())) ;
			
			request.setAttribute("item", item) ;
			
			return new TopRecordForm(record, false) ;
		}else{
			item = statItemManager.getForRead(statId) ;
			Assert.assertResouceNotNull(item, "统计项不存在！") ;
			
			this.sessionManager.assertCommiter(loginUser, Constants.serviceName.STAT_ITEM, String.valueOf(item.getGroupId())) ;
			
			request.setAttribute("item", item) ;
			
			record = new TopRecord() ;
			record.setStatId(statId) ;
			record.setGroupId(item.getGroupId()) ;
			record.setBanned(false) ;
			record.setObjectOrder(1) ;
			record.setObjectCreatedTime(DateUtil.date2String(new Date())) ;
			
			return new TopRecordForm(record, true) ;
		}
	}

	public ITopRecordManager getTopRecordManager() {
		return topRecordManager;
	}

	public void setTopRecordManager(ITopRecordManager topRecordManager) {
		this.topRecordManager = topRecordManager;
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
