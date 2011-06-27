package com.guzzservices.business;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;

import org.guzz.annotations.GenericGenerator;
import org.guzz.annotations.Parameter;
import org.guzz.annotations.Table;

/**
 * 配置信息组
 * 
 * @author liu kaixuan
 */
@javax.persistence.Entity 
@Table(name="gs_configuration_group")
public class ConfigurationGroup implements Serializable {

	private String id ;
	
	private int userId ;
	
	private String name ;
	
	private Date createdTime ;
	
	private int version ;

	@javax.persistence.Id
	@GenericGenerator(name="randomGen", strategy="random", parameters={@Parameter(name="length", value="64") })
	@GeneratedValue(generator="randomGen")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String groupName) {
		this.name = groupName;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	@Column(updatable=false)
	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	@Column(updatable=false)
	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}
	
}
