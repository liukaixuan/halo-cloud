package com.guzzservices.business;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import org.guzz.annotations.Table;

/**
 * 统计日志表，记录每一个时间点数据的操作记录。
 * 
 * @author liu kaixuan
 */
@javax.persistence.Entity 
@Table(name="gs_stat_log")
public class StatLog implements Serializable {
	
	/**发布数据*/
	public static final int OP_PUBLISH_DATA = 0 ;
	
	/**加载数据到统计临时表中*/
	public static final int OP_REFRESH_DB_DATA = 1 ;

	private int id ;
	
	private int userId ;
	
	private int statId ;
	
	/**执行时间*/
	private Date statExecuteTime ;
	
	private int type ;
	
	/**执行结果。如果是load，记录读取到的原始排行数据；如果是publish，记录为发布的内容。*/
	private String result ;

	@javax.persistence.Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getStatExecuteTime() {
		return statExecuteTime;
	}

	public void setStatExecuteTime(Date statExecuteTime) {
		this.statExecuteTime = statExecuteTime;
	}

	public int getStatId() {
		return statId;
	}

	public void setStatId(int statId) {
		this.statId = statId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

}
