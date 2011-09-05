package com.guzzservices.manager.impl;

import java.io.Serializable;
import java.util.concurrent.CountDownLatch;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.guzz.exception.GuzzException;
import org.guzz.util.Assert;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.TriggerKey;
import org.quartz.spi.MutableTrigger;

import com.guzzservices.business.Task;
import com.guzzservices.manager.ITaskManager;

/**
 * 
 * 
 * @author liu kaixuan
 */
public class TaskManagerImpl extends AbstractBaseManagerImpl<Task> implements ITaskManager {
	static final Log log = LogFactory.getLog(TaskManagerImpl.class) ;
	
	public Scheduler scheduler ;
	
	public static ITaskManager taskManager ;
	
	static CountDownLatch waitingForInit = new CountDownLatch(1) ;
	
	protected String fillCronWithSeconds(String cron){
		int seconds = (int) (Math.random() * 60) ;
		
		return seconds + " " + cron ;
	}

	public void delete(Object domainObject) {
		Task m_task = (Task) domainObject ;
		if(m_task == null) return ;
		
		//remove to task
		try {
			scheduler.deleteJob(JobKey.jobKey(m_task.getTaskName(), m_task.getTaskGroupName())) ;
		} catch (Exception e) {			
			throw new GuzzException("taskId:" + m_task.getId(), e) ;
		}

		super.delete(domainObject);
	}
	
	public void reScheduleTask(Task task, String newCron){
		if(task == null) return ;
		
		try {
			//得到trigger
			MutableTrigger trigger = CronScheduleBuilder.cronSchedule(fillCronWithSeconds(newCron)).build() ;
			trigger.setKey(TriggerKey.triggerKey(task.getTaskName(), task.getTaskGroupName())) ;
			trigger.setJobKey(JobKey.jobKey(task.getTaskName(), task.getTaskGroupName())) ;
			
			// 重置job
			scheduler.rescheduleJob(trigger.getKey(), trigger);
		} catch (Exception e) {			
			throw new GuzzException("taskId:" + task.getId(), e) ;
		}
		
		task.setCronExpression(newCron) ;
		this.update(task) ;
	}

	public Serializable insert(Object domainObject) {
		Task m_task = (Task) domainObject ;
		Assert.assertNotEmpty(m_task.getCronExpression(), "cron cann't be null!") ;
		
		super.insert(domainObject);
		
		//add to task
		try {
			JobDetail job = JobBuilder.newJob(ExecuteTaskJob.class).withIdentity(m_task.getTaskName(), m_task.getTaskGroupName()).build() ;
			job.getJobDataMap().put("taskId", m_task.getId()) ;
			
			MutableTrigger trigger = CronScheduleBuilder.cronSchedule(fillCronWithSeconds(m_task.getCronExpression())).build() ;
			trigger.setKey(TriggerKey.triggerKey(m_task.getTaskName(), m_task.getTaskGroupName())) ;
			trigger.setJobKey(job.getKey()) ;
			
			scheduler.scheduleJob(job, trigger) ;
		} catch (Exception e) {
			//删掉
			super.delete(domainObject) ;
			
			throw new GuzzException("taskId:" + m_task.getId(), e) ;
		}
		
		return m_task.getId() ;
	}

	public Scheduler getScheduler() {
		return scheduler;
	}

	public void setScheduler(Scheduler scheduler) {
		this.scheduler = scheduler;
		taskManager = this ;
		waitingForInit.countDown() ;
		waitingForInit = null ;
	}

}
