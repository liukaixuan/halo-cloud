package com.guzzservices.manager.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.guzz.util.StringUtil;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.guzzservices.business.Task;
import com.guzzservices.util.HttpClientUtils;

public class ExecuteTaskJob implements Job{

	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDataMap data = context.getJobDetail().getJobDataMap() ;
		
		int taskId = data.getInt("taskId") ;
		if(TaskManagerImpl.log.isDebugEnabled()){
			TaskManagerImpl.log.debug("begin task:" + taskId) ;
		}
		
		if(TaskManagerImpl.waitingForInit != null){
			try {
				TaskManagerImpl.waitingForInit.await(120, TimeUnit.SECONDS) ;
			} catch (InterruptedException e) {
				throw new JobExecutionException(e) ;
			}
		}
		
		Task m_task = TaskManagerImpl.taskManager.getForUpdate(taskId) ;
		
		if(m_task == null){
			TaskManagerImpl.log.warn("unknown task:" + taskId) ;
			return ;
		}
		
		if(!m_task.shouldExecute()){
			if(TaskManagerImpl.log.isDebugEnabled()){
				TaskManagerImpl.log.debug("task:" + taskId + " cancelled. Not in a execute time span.") ;
			}
			
			return ;
		}
		
		//request a remote page
		HashMap<String, String> params = new HashMap<String, String>() ;
		params.put("authKey", m_task.getAuthKey()) ;
		
		String resultCode = null ;
		try {
			resultCode = HttpClientUtils.get(m_task.getRemoteUrl(), params, "UTF-8");
		} catch (Exception e) {
			TaskManagerImpl.log.error("fail to execute task:" + m_task.getId(), e) ;
			
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
		
		if(TaskManagerImpl.log.isDebugEnabled()){
			TaskManagerImpl.log.debug("task:" + taskId + " executed. result:" + resultCode) ;
		}
		
		TaskManagerImpl.taskManager.update(m_task) ;
	}
}