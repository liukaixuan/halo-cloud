package com.guzzservices.business;

import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import org.guzz.annotations.Table;

/**
 * 过滤词
 * 
 * @author liu kaixuan
 */
@javax.persistence.Entity 
@Table(name="gs_filter_word")
public class FilterWord {
	
	private int id ;
	
	private String word ;
	
	/**过滤词所在组，程序中的组名*/
	private String groupId ;
	
	/**过滤词的严重等级，不同性质的过滤词对应的等级有可能是不同的*/
	private int level = 3 ;
	
	/**记录过滤词最后更新时间*/
	private Date createdTime ;

	@javax.persistence.Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

}
