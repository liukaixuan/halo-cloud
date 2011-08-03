package com.guzzservices.business;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import org.guzz.annotations.Table;

/**
 * 
 * 
 * @author liu kaixuan
 */
@javax.persistence.Entity 
@Table(name="gs_top_record")
public class TopRecord implements Serializable {

	private long id ;
	
	/**统计对象的主id*/
	private String objectId ;
	
	/**统计项目的id, foreign key to StatItem(id) */
	private int statId ;
	
	private int groupId ;
		
	/**操作次数，将按照此字段进行排序*/
	private int opTimes ;
		
	/**是否在前台隐藏该统计项*/
	private boolean banned ;
	
	/**
	 * 对象的排列顺序，前台显示时将按照这个顺序显示，其中1表示排在第一位。
	 * 顺序是和timePoint objectName相关的。数据库内将按照<b>所有</b>category的内容进行大排序。
	 */
	private int objectOrder ;
	
	//添加一些常用显示字段
	private String objectTitle ;
	
	/**对象细览地址*/
	private String objectURL ;
	
	private String objectCreatedTime ;
	
	/**自定义字段，每个统计对象可以添加一些其他信息供自己展示使用*/
	private String extra1 ;
	
	private String extra2 ;
	
	private String extra3 ;

	@javax.persistence.Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getObjectOrder() {
		return objectOrder;
	}

	public void setObjectOrder(int objectOrder) {
		this.objectOrder = objectOrder;
	}

	public int getOpTimes() {
		return opTimes;
	}

	public void setOpTimes(int opTimes) {
		this.opTimes = opTimes;
	}

	public int getStatId() {
		return statId;
	}

	public void setStatId(int statId) {
		this.statId = statId;
	}

	public String getObjectCreatedTime() {
		return objectCreatedTime;
	}

	public void setObjectCreatedTime(String objectCreatedTime) {
		this.objectCreatedTime = objectCreatedTime;
	}

	public String getObjectTitle() {
		return objectTitle;
	}

	public void setObjectTitle(String objectTitle) {
		this.objectTitle = objectTitle;
	}

	public String getObjectURL() {
		return objectURL;
	}

	public void setObjectURL(String objectURL) {
		this.objectURL = objectURL;
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public String getExtra1() {
		return extra1;
	}

	public void setExtra1(String extra1) {
		this.extra1 = extra1;
	}

	public String getExtra2() {
		return extra2;
	}

	public void setExtra2(String extra2) {
		this.extra2 = extra2;
	}

	public String getExtra3() {
		return extra3;
	}

	public void setExtra3(String extra3) {
		this.extra3 = extra3;
	}

	public boolean isBanned() {
		return banned;
	}

	public void setBanned(boolean banned) {
		this.banned = banned;
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

}
