package com.guzzservices.manager.impl;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.guzz.exception.GuzzException;
import org.guzz.transaction.WriteTranSession;
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

import com.guzzservices.business.BannedTopRecord;
import com.guzzservices.business.StatItem;
import com.guzzservices.business.StatLog;
import com.guzzservices.business.TopRecord;
import com.guzzservices.manager.IBannedTopRecordManager;
import com.guzzservices.manager.IStatItemManager;
import com.guzzservices.manager.ITopRecordManager;
import com.guzzservices.manager.impl.top.TopDataProvider;
import com.guzzservices.manager.impl.top.TopDataPublisher;
import com.guzzservices.rpc.util.JsonUtil;
import com.guzzservices.velocity.VelocityService;

/**
 * 
 * 
 * @author liu kaixuan
 */
public class StatItemManagerImpl extends AbstractBaseManagerImpl<StatItem> implements IStatItemManager {
	private static final Log log = LogFactory.getLog(StatItemManagerImpl.class) ;
	
	private ITopRecordManager topRecordManager ;
	
	private TopDataProvider topDataProvider ;
	
	private TopDataPublisher topDataPublisher ;
	
	private IBannedTopRecordManager bannedTopRecordManager ;
	
	private VelocityService velocityService ;
	
	public Scheduler scheduler ;
	
	public static IStatItemManager statItemManager ;
	
	private static CountDownLatch waitingForInit = new CountDownLatch(1) ;
	
	public static class TopRankJob implements Job{

		public void execute(JobExecutionContext context) throws JobExecutionException {
			int statId = context.getJobDetail().getJobDataMap().getInt("statId") ;
			
			if(waitingForInit != null){
				try {
					waitingForInit.await() ;
				} catch (InterruptedException e) {
					throw new JobExecutionException(e) ;
				}
			}
			
			StatItem si = statItemManager.getForRead(statId) ;
			
			if(si == null){
				log.warn("unknown stat:" + statId) ;
				return ;
			}
			
			if(!si.shouldExecute()){
				if(log.isDebugEnabled()){
					log.debug("stat:" + statId + " cancelled. Not in a execute time span.") ;
				}
				
				return ;
			}
			
			if(si.getCanProvide()){
				statItemManager.refreshRecords(si) ;
			}
			
			if(si.isAutoPublish() && si.getCanPublish()){
				String text = statItemManager.publishRecords(si) ;
				
				if(log.isDebugEnabled()){
					log.debug("publish stat: " + statId + "; content:" + text) ;
				}
			}
		}
	}

	public void delete(Object domainObject) {
		StatItem si = (StatItem) domainObject ;
		
		//remove to task
		try {
			scheduler.deleteJob(JobKey.jobKey(si.getTaskName(), si.getTaskGroupName())) ;
		} catch (Exception e) {			
			throw new GuzzException("statId:" + si.getId(), e) ;
		}

		super.delete(domainObject);
	}
	
	public void reScheduleTask(StatItem si, String newCron){
		if(si == null) return ;
		
		try {
			 // 得到trigger
			MutableTrigger trigger = CronScheduleBuilder.cronSchedule(fillCronWithSeconds(newCron)).build() ;
			trigger.setKey(TriggerKey.triggerKey(si.getTaskName(), si.getTaskGroupName())) ;
			trigger.setJobKey(JobKey.jobKey(si.getTaskName(), si.getTaskGroupName())) ;
			
			// 重置job
			scheduler.rescheduleJob(trigger.getKey(), trigger);
		} catch (Exception e) {			
			throw new GuzzException("StatItem id:" + si.getId(), e) ;
		}
		
		si.setCronExpression(newCron) ;
		this.update(si) ;
	}
	
	protected String fillCronWithSeconds(String cron){
		int seconds = (int) (Math.random() * 60) ;
		
		return seconds + " " + cron ;
	}

	public Serializable insert(Object domainObject) {
		super.insert(domainObject);
		
		StatItem si = (StatItem) domainObject ;
		
		//add to task
		if(StringUtil.notEmpty(si.getCronExpression())){
			try {
				//CronTriggerImpl trigger = new CronTriggerImpl(si.getTaskName(), si.getTaskGroupName(), fillCronWithSeconds(si.getCronExpression()));
				JobDetail job = JobBuilder.newJob(TopRankJob.class).withIdentity(si.getTaskName(), si.getTaskGroupName()).build() ;
				job.getJobDataMap().put("statId", si.getId()) ;
				
				MutableTrigger trigger = CronScheduleBuilder.cronSchedule(fillCronWithSeconds(si.getCronExpression())).build() ;
				trigger.setKey(TriggerKey.triggerKey(si.getTaskName(), si.getTaskGroupName())) ;
				trigger.setJobKey(job.getKey()) ;
				
				scheduler.scheduleJob(job, trigger) ;
			} catch (Exception e) {
				//删掉
				super.delete(domainObject) ;
				
				throw new GuzzException("statId:" + si.getId(), e) ;
			}
		}
		
		return si.getId() ;
	}
	
	public void setException(StatItem item, Exception e) {
		item.setErrorInfo(e == null ? null : e.getMessage()) ;
		
		super.update(item) ;
	}
	
	public String publishRecords(StatItem item){
		List<TopRecord> records = this.topRecordManager.listCleanRecords(item, item.getPublishSize()) ;
		
		HashMap data = new HashMap() ;
		data.put("statItem", item) ;
		data.put("records", records) ;
		
		String text;
		try {
			text = this.velocityService.translateText(data, "stat" + item.getId(), item.getTemplateContent());
		} catch (Exception e) {
			this.setException(item, e) ;
			
			return "error:" + e.getMessage() ;
		}
		
		StatLog log = new StatLog() ;
		log.setStatExecuteTime(new Date()) ;
		log.setStatId(item.getId()) ;
		log.setType(StatLog.OP_PUBLISH_DATA) ;
		log.setResult(text) ;
		log.setUserId(item.getUserId()) ;
		
		super.insert(log) ;
		
		//通知使用者，重新加载内容。
		try {
			this.topDataPublisher.publishData(item, text) ;
		} catch (Exception e) {
			this.setException(item, e) ;
			
			return "error:" + e.getMessage() ;
		}
		
		return text ;
	}
	
	public void refreshRecords(StatItem item){
		List<TopRecord> records;
		try {
			records = this.topDataProvider.readNewData(topRecordManager, this, item);
		} catch (Exception e1) {
			this.setException(item, e1) ;
			
			return ;
		}
		
		//关联数据
		for(TopRecord r : records){
			r.setStatId(item.getId()) ;
			r.setGroupId(item.getGroupId()) ;
			
			BannedTopRecord btr = this.topRecordManager.getBannedTopRecord(r.getObjectId(), r.getGroupId()) ;
			
			if(btr != null){
				r.setBanned(true) ;
				
				btr.setLastHitTime(new Date()) ;
				super.update(btr) ;
			}
		}
		
		//进行排序
		Collections.sort(records, new Comparator<TopRecord>() {

			public int compare(TopRecord r1, TopRecord r2) {
				return r2.getOpTimes() - r1.getOpTimes() ;
			}
		}) ;
		
		item.setLastDataLoadTime(new Date()) ;
		
		StatLog log = new StatLog() ;
		log.setStatExecuteTime(item.getLastDataLoadTime()) ;
		log.setStatId(item.getId()) ;
		log.setType(StatLog.OP_REFRESH_DB_DATA) ;
		log.setResult(JsonUtil.toJson(records)) ;
		log.setUserId(item.getUserId()) ;
		
		WriteTranSession write = this.getTransactionManager().openRWTran(false) ;
		
		try{
			write.insert(log) ;
			this.topRecordManager.cleanUpOldData(write, item) ;
			
			for(int i = 0 ; i < records.size() ; i++){
				TopRecord r = records.get(i) ;
				r.setObjectOrder(i + 1) ;
				
				write.insert(r) ;
			}
			
			write.update(item) ;
			
			write.commit() ;
		}catch(Exception e){
			write.rollback() ;
			
			this.setException(item, e) ;
		}finally{
			write.close() ;
		}
	}

	public ITopRecordManager getTopRecordManager() {
		return topRecordManager;
	}

	public void setTopRecordManager(ITopRecordManager topRecordManager) {
		this.topRecordManager = topRecordManager;
	}

	public TopDataProvider getTopDataProvider() {
		return topDataProvider;
	}

	public void setTopDataProvider(TopDataProvider topDataProvider) {
		this.topDataProvider = topDataProvider;
	}

	public TopDataPublisher getTopDataPublisher() {
		return topDataPublisher;
	}

	public void setTopDataPublisher(TopDataPublisher topDataPublisher) {
		this.topDataPublisher = topDataPublisher;
	}

	public IBannedTopRecordManager getBannedTopRecordManager() {
		return bannedTopRecordManager;
	}

	public void setBannedTopRecordManager(IBannedTopRecordManager bannedTopRecordManager) {
		this.bannedTopRecordManager = bannedTopRecordManager;
	}

	public VelocityService getVelocityService() {
		return velocityService;
	}

	public void setVelocityService(VelocityService velocityService) {
		this.velocityService = velocityService;
	}

	public Scheduler getScheduler() {
		return scheduler;
	}

	public void setScheduler(Scheduler scheduler) {
		this.scheduler = scheduler;
		statItemManager = this ;
		
		waitingForInit.countDown() ;
		waitingForInit = null ;
	}

}
