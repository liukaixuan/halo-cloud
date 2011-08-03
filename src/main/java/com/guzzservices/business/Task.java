package com.guzzservices.business;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Transient;

import org.guzz.annotations.Table;
import org.guzz.util.DateUtil;

/**
 * 
 * 调度任务。主要用于定时的循环请求某一个网页地址，用于通知。
 * 
 * @author liu kaixuan
 */
@javax.persistence.Entity 
@Table(name="gs_task")
public class Task implements Serializable {

	private int id ;
	
	private String name ;
	
	private String authKey ;
	
	private int userId ;
		
	private String remoteUrl ;
		
	/**cron表达式*/
	private String cronExpression = "0 * * * ? *" ;
	
	/**统计项所在组，方便管理之用*/
	private int groupId ;
		
	/**任务错误时的错误信息*/
	private int errorCode ;
	
	private Date lastExecuteTime ;
	
	private Date lastSucessTime ;
	
	@Transient
	public String getTaskName(){
		return "task" + id ;
	}
	
	@Transient
	public String getTaskGroupName(){
		return "task" + groupId ;
	}

	@javax.persistence.Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	/**根据上次执行时间与timePointPrecision，判断当前时间是否应该执行。*/
	public boolean shouldExecute(){
		if(this.lastExecuteTime == null) return true ;
		
		return !DateUtil.isDateClose(lastExecuteTime, new Date(), 30) ;
	}

	public String getAuthKey() {
		return authKey;
	}

	public void setAuthKey(String authKey) {
		this.authKey = authKey;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getRemoteUrl() {
		return remoteUrl;
	}

	public void setRemoteUrl(String remoteUrl) {
		this.remoteUrl = remoteUrl;
	}

	public String getCronExpression() {
		return cronExpression;
	}

	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public Date getLastExecuteTime() {
		return lastExecuteTime;
	}

	public void setLastExecuteTime(Date lastExecuteTime) {
		this.lastExecuteTime = lastExecuteTime;
	}

	public Date getLastSucessTime() {
		return lastSucessTime;
	}

	public void setLastSucessTime(Date lastSucessTime) {
		this.lastSucessTime = lastSucessTime;
	}
	
}
