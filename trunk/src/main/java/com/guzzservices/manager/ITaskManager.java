package com.guzzservices.manager;

import com.guzzservices.business.Task;

/**
 * 
 * 
 * @author liu kaixuan
 */
public interface ITaskManager extends IBaseManager<Task> {
	
	public void reScheduleTask(Task task, String newCron) ;
	
}
