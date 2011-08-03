/**
 * 
 */
package com.guzzservices.action.console.task;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.guzz.util.RequestUtil;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.guzzservices.business.TaskGroup;
import com.guzzservices.manager.Constants;
import com.guzzservices.manager.ISessionManager;
import com.guzzservices.manager.ITaskGroupManager;
import com.guzzservices.sso.LoginUser;

/**
 * 
 * 
 * 
 * @author liukaixuan(liukaixuan@gmail.com)
 */
public class TaskGroupAction implements Controller {
	
	private ITaskGroupManager taskGroupManager ;
	
	private ISessionManager sessionManager ;

	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		LoginUser loginUser = sessionManager.getLoginUser(request, response) ;
		int groupId = RequestUtil.getParameterAsInt(request, "groupId", 0) ;
		String groupName = request.getParameter("groupName") ;
		
		if(groupId > 0){
			this.sessionManager.assertOwner(loginUser, Constants.serviceName.TASK, String.valueOf(groupId)) ;
			
			//edit
			TaskGroup m_group = this.taskGroupManager.getForUpdate(groupId) ;
			
			if(m_group == null){
				return null ;
			}
			
			m_group.setName(groupName) ;
			
			this.taskGroupManager.update(m_group) ;
		}else{
			TaskGroup m_group = new TaskGroup() ;
			m_group.setCreatedTime(new Date()) ;
			m_group.setName(groupName) ;
			m_group.setUserId(loginUser.getUserId()) ;
			
			this.taskGroupManager.add(loginUser, m_group) ;
		}
		
		return null;
	}

	public ITaskGroupManager getTaskGroupManager() {
		return taskGroupManager;
	}

	public void setTaskGroupManager(ITaskGroupManager taskGroupManager) {
		this.taskGroupManager = taskGroupManager;
	}

	public ISessionManager getSessionManager() {
		return sessionManager;
	}

	public void setSessionManager(ISessionManager sessionManager) {
		this.sessionManager = sessionManager;
	}

}
