package com.guzzservices.manager.impl;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.guzz.exception.GuzzException;
import org.guzz.util.Assert;
import org.guzz.util.StringUtil;
import org.quartz.CronScheduleBuilder;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.TriggerKey;
import org.quartz.spi.MutableTrigger;

import com.guzzservices.business.Task;
import com.guzzservices.manager.ITaskManager;
import com.guzzservices.manager.impl.StatItemManagerImpl.TopRankJob;
import com.guzzservices.util.HttpClientUtils;

/**
 * 
 * 
 * @author liu kaixuan
 */
public class TaskManagerImpl extends AbstractBaseManagerImpl<Task> implements ITaskManager {
	private static final Log log = LogFactory.getLog(TaskManagerImpl.class) ;
	
	public Scheduler scheduler ;
	
	public static ITaskManager taskManager ;
	
	private static CountDownLatch waitingForInit = new CountDownLatch(1) ;
	
	public static class ExecuteTaskJob implements Job{

		public void execute(JobExecutionContext context) throws JobExecutionException {
			int taskId = context.getJobDetail().getJobDataMap().getInt("taskId") ;
			
			if(waitingForInit != null){
				try {
					waitingForInit.await() ;
				} catch (InterruptedException e) {
					throw new JobExecutionException(e) ;
				}
			}
			
			Task m_task = taskManager.getForUpdate(taskId) ;
			
			if(m_task == null){
				log.warn("unknown task:" + taskId) ;
				return ;
			}
			
			if(!m_task.shouldExecute()){
				if(log.isDebugEnabled()){
					log.debug("task:" + taskId + " cancelled. Not in a execute time span.") ;
				}
				
				return ;
			}
			
			//request a remote page
			HashMap<String, String> params = new HashMap<String, String>() ;
			params.put("authKey", m_task.getAuthKey()) ;
			
			String resultCode = null ;
			try {
				resultCode = HttpClientUtils.get(m_task.getRemoteUrl(), params, "UTF-8");
			} catch (IOException e) {
				throw new JobExecutionException("fail to execute task:" + m_task.getId(), e) ;
			}
			
			if(resultCode != null){
				resultCode = resultCode.trim() ;
			}
			
			int code = StringUtil.toInt(resultCode) ;
			m_task.setErrorCode(code) ;
			m_task.setLastExecuteTime(new Date()) ;
			
			if(code == 0){
				m_task.setLastSucessTime(m_task.getLastExecuteTime()) ;
			}
			
			if(log.isDebugEnabled()){
				log.debug("task:" + taskId + " executed. result:" + resultCode) ;
			}
			
			taskManager.update(m_task) ;
		}
	}
	
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
			JobDetail job = JobBuilder.newJob(TopRankJob.class).withIdentity(m_task.getTaskName(), m_task.getTaskGroupName()).build() ;
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
