/**
 * 
 */
package com.guzzservices.business;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import org.guzz.annotations.Table;

/**
 * 
 * 以前被隐藏过的排行记录。同一个groupId_objectId只保存一份。
 * 
 * @author liukaixuan(liukaixuan@gmail.com)
 */
@javax.persistence.Entity 
@Table(name="gs_banned_top_record")
public class BannedTopRecord implements Serializable {
	
	private long id ;
	
	/**统计组*/
	private int groupId ;
	
	private String objectId ;
	
	private String objectTitle ;
	
	private String objectURL ;
	
	/**排行榜最后一次匹配到此记录的时间*/
	private Date lastHitTime ;
	
	private Date createdTime ;
	
	@javax.persistence.Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public String getObjectTitle() {
		return objectTitle;
	}

	public void setObjectTitle(String objectTitle) {
		this.objectTitle = objectTitle;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Date getLastHitTime() {
		return lastHitTime;
	}

	public void setLastHitTime(Date lastHitTime) {
		this.lastHitTime = lastHitTime;
	}

	public String getObjectURL() {
		return objectURL;
	}

	public void setObjectURL(String objectURL) {
		this.objectURL = objectURL;
	}

}
