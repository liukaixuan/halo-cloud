/**
 * 
 */
package com.guzzservices.business;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import org.guzz.annotations.Table;

/**
 * 
 * 使用日志的应用。
 * 
 * @author liukaixuan(liukaixuan@gmail.com)
 */
@javax.persistence.Entity 
@Table(name="gs_log_app")
public class LogApp {
	
	private int id ;

	private String appName ;
	
	private String secureCode ;
	
	private String description ;
	
	private long recordsCount ;
	
	private Date createdTime ;

	@javax.persistence.Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getSecureCode() {
		return secureCode;
	}

	public void setSecureCode(String secureCode) {
		this.secureCode = secureCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	@Column(updatable=false)
	public long getRecordsCount() {
		return recordsCount;
	}

	public void setRecordsCount(long recordsCount) {
		this.recordsCount = recordsCount;
	}
	
}
