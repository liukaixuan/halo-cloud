package com.guzzservices.business;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Transient;

import org.guzz.annotations.Table;
import org.guzz.util.DateUtil;
import org.guzz.util.StringUtil;

/**
 * 
 * 统计的内容。每一项内容占用一个。
 * 
 * @author liu kaixuan
 */
@javax.persistence.Entity 
@Table(name="gs_stat_item")
public class StatItem implements Serializable {
	
	public static final int TIME_POINT_PRECISION_MINUTE = 1 ;
	
	public static final int TIME_POINT_PRECISION_TEN_MINUTES = 10 ;
	
	public static final int TIME_POINT_PRECISION_HOUR = 60 ;
	
	public static final int TIME_POINT_PRECISION_DAY = 1440 ;
		
	private int id ;
	
	private String name ;
	
	private String authKey ;
	
	private int userId ;
	
	private String programId ;
	
	private String dataProviderUrl ;
	
	private String encoding = "UTF-8" ;
	
	private String dataPublisherUrl ;
	
	/**上次数据加载执行的时间*/
	private Date lastDataLoadTime ;
		
	/**时间点的计算的精度，通过时间点来避免多机器情况下重复统计同一个项目*/
	private int timePointPrecision = TIME_POINT_PRECISION_TEN_MINUTES ;
	
	/**任务是否自动发布*/
	private boolean autoPublish ;
		
	/**cron表达式*/
	private String cronExpression = "0 * * * ? *" ;
	
	/**读取的记录数*/
	private int fetchSize = 25 ;
	
	/**发布的记录数*/
	private int publishSize = 20 ;
	
	/**
	 * 记录排行中被隐藏的记录，下次生成新的排行时，如果再次出现，默认自动设置为隐藏。
	 */
	private boolean recordCheating = true ;	
	
	/**统计最近多少分钟的数据*/
	private int statBeforeMinutes = 1440 ; /**一天*/
	
	/**统计项所在组，方便管理之用*/
	private int groupId ;
		
	/**任务错误时的错误信息*/
	private String errorInfo ;
	
	private String description ;
	
	/**临时表中的数据是否允许修改*/
	private boolean recordEditable ;
	
	private String templateContent ;
	
	@Transient
	public String getTaskName(){
		return "stat" + id ;
	}
	
	@Transient
	public String getTaskGroupName(){
		return "top" + userId ;
	}

	public boolean isAutoPublish() {
		return autoPublish;
	}

	public void setAutoPublish(boolean autoPublish) {
		this.autoPublish = autoPublish;
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

	public int getTimePointPrecision() {
		return timePointPrecision;
	}

	public void setTimePointPrecision(int timePointPrecision) {
		this.timePointPrecision = timePointPrecision;
	}	
	
	/**根据上次执行时间与timePointPrecision，判断当前时间是否应该执行。*/
	public boolean shouldExecute(){
		if(this.lastDataLoadTime == null) return true ;
		
		return !DateUtil.isDateClose(lastDataLoadTime, new Date(), timePointPrecision * 60) ;
	}

	public String getErrorInfo() {
		return errorInfo;
	}

	public void setErrorInfo(String errorInfo) {
		this.errorInfo = errorInfo;
	}

	public int getFetchSize() {
		return fetchSize;
	}

	public void setFetchSize(int pageSize) {
		this.fetchSize = pageSize;
	}

	public String getCronExpression() {
		return cronExpression;
	}

	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String toString() {
		return name ;
	}

	public int getPublishSize() {
		return publishSize;
	}

	public void setPublishSize(int publishSize) {
		this.publishSize = publishSize;
	}

	/**统计最近多少多少分钟的数据*/
	public int getStatBeforeMinutes() {
		return statBeforeMinutes;
	}

	public void setStatBeforeMinutes(int statBeforeMinutes) {
		this.statBeforeMinutes = statBeforeMinutes;
	}

	/**是否可以发布数据*/
	@Transient
	public boolean getCanPublish(){
		return StringUtil.notEmpty(dataPublisherUrl) ;
	}
	
	/**是否可以读取数据*/
	@Transient
	public boolean getCanProvide(){
		return StringUtil.notEmpty(dataProviderUrl) ;
	}

	public boolean isRecordEditable() {
		return recordEditable;
	}

	public void setRecordEditable(boolean recordEditable) {
		this.recordEditable = recordEditable;
	}

	public String getDataProviderUrl() {
		return dataProviderUrl;
	}

	public void setDataProviderUrl(String dataProviderUrl) {
		this.dataProviderUrl = dataProviderUrl;
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

	public Date getLastDataLoadTime() {
		return lastDataLoadTime;
	}

	public void setLastDataLoadTime(Date lastDataLoadTime) {
		this.lastDataLoadTime = lastDataLoadTime;
	}

	public boolean isRecordCheating() {
		return recordCheating;
	}

	public void setRecordCheating(boolean recordCheating) {
		this.recordCheating = recordCheating;
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public String getTemplateContent() {
		return templateContent;
	}

	public void setTemplateContent(String templateContent) {
		this.templateContent = templateContent;
	}

	public String getProgramId() {
		return programId;
	}

	public void setProgramId(String programId) {
		this.programId = programId;
	}

	public String getDataPublisherUrl() {
		return dataPublisherUrl;
	}

	public void setDataPublisherUrl(String dataPublisherUrl) {
		this.dataPublisherUrl = dataPublisherUrl;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}
	
}
