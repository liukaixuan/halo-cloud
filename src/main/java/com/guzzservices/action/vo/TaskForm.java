package com.guzzservices.action.vo;

import com.guzzservices.business.Task;
import com.guzzservices.util.RandomUtils;

/**
 * 
 * 
 * @author liu kaixuan
 */
public class TaskForm {

	private Task task ;	
	private boolean newTask ;
		
	public TaskForm(Task task){
		newTask = false ;
		this.task = task ;
	}
	
	public TaskForm(int groupId, int userId){
		newTask = true ;
		this.task = new Task() ;
		this.task.setGroupId(groupId) ;
		this.task.setUserId(userId) ;
		this.task.setAuthKey(RandomUtils.generateRandomString(32)) ;
	}

	public boolean isNewTask() {
		return newTask;
	}

	public void setNewTask(boolean newTask) {
		this.newTask = newTask;
	}

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}
	
}
