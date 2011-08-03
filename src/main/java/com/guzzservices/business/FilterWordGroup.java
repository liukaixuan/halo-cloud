package com.guzzservices.business;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;

import org.guzz.annotations.GenericGenerator;
import org.guzz.annotations.Parameter;
import org.guzz.annotations.Table;

/**
 * 过滤词组
 * 
 * @author liu kaixuan
 */
@javax.persistence.Entity 
@Table(name="gs_filter_word_group")
public class FilterWordGroup implements Serializable {

	private String id ;
	
	private int userId ;
	
	private String name ;
	
	private String color ;
	
	private String description ;
	
	private Date createdTime ;

	@javax.persistence.Id
	@GenericGenerator(name="randomGen", strategy="random", parameters={@Parameter(name="length", value="40") })
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

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
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
	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}
	
}
