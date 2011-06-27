package com.guzzservices.action.console.task;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.guzz.util.RequestUtil;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import com.guzzservices.action.vo.TaskForm;
import com.guzzservices.business.Task;
import com.guzzservices.manager.Constants;
import com.guzzservices.manager.ISessionManager;
import com.guzzservices.manager.ITaskManager;
import com.guzzservices.sso.LoginUser;

/**
 * 
 * 
 * @author liu kaixuan
 */
public class TaskAction extends SimpleFormController {

	private ITaskManager taskManager ;
	
	private ISessionManager sessionManager ;
	
	public TaskAction() {
		this.setCommandName("taskForm") ;
	}
	
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		TaskForm itemForm = (TaskForm) command ;
		Task item = itemForm.getTask() ;
		
		if(itemForm.isNewTask()){
			taskManager.insert(item) ;
		}else{
			taskManager.update(item) ;
		}
		
		return new ModelAndView(getSuccessView(), "groupId", item.getGroupId()) ;
	}
	
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		LoginUser loginUser = sessionManager.getLoginUser(request, null) ;
		int id = RequestUtil.getParameterAsInt(request, "id", -1) ;
		String groupId = request.getParameter("groupId") ;
		
		Task m_task = null ;
		
		if(id > 0){
			m_task = taskManager.getForUpdate(id) ;
			this.sessionManager.assertOwner(loginUser, Constants.serviceName.TASK, String.valueOf(m_task.getGroupId())) ;
			
			return new TaskForm(m_task) ;
		}else{
			this.sessionManager.assertOwner(loginUser, Constants.serviceName.TASK, groupId) ;
			
			return new TaskForm(Integer.parseInt(groupId), loginUser.getUserId()) ;
		}
	}

	public ITaskManager getTaskManager() {
		return taskManager;
	}

	public void setTaskManager(ITaskManager taskManager) {
		this.taskManager = taskManager;
	}

	public ISessionManager getSessionManager() {
		return sessionManager;
	}

	public void setSessionManager(ISessionManager sessionManager) {
		this.sessionManager = sessionManager;
	}

}
