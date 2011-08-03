package com.guzzservices.business;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import org.guzz.annotations.Table;

/**
 * 
 * 统计项的用户分组。
 * 
 * @author liu kaixuan
 */
@javax.persistence.Entity 
@Table(name="gs_stat_item_group")
public class StatItemGroup implements Serializable {
		
	private int id ;
	
	private String name ;
	
	private int userId ;
	
	private Date createdTime ;

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

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}
	
}
