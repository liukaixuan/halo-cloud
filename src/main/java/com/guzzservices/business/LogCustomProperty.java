/**
 * 
 */
package com.guzzservices.business;

import java.util.Date;

import org.guzz.annotations.Table;

/**
 * 
 * 日志记录中的自定义字段。
 * 
 * @author liukaixuan(liukaixuan@gmail.com)
 */
@javax.persistence.Entity 
@Table(name="gs_log_custom_property")
public class LogCustomProperty {

	/**Unique id for management.*/
	private int id ;
	
	/**which LogApp this property belongs to.*/
	private int appId ;
	
	/**Java中使用的属性名*/
	private String propName ;
	
	/**在数据库中存储的字段名*/
	private String colName ;
	
	/**对普通用户显示的名称*/
	private String displayName ;
	
	/**
	 * dataType. Take this as the 'type' property in hbm.xml file.
	 */
	private String dataType ;
	
	private Date createdTime ;

	@javax.persistence.Id
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPropName() {
		return propName;
	}

	public void setPropName(String propName) {
		this.propName = propName;
	}

	public String getColName() {
		return colName;
	}

	public void setColName(String colName) {
		this.colName = colName;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public int getAppId() {
		return appId;
	}

	public void setAppId(int appId) {
		this.appId = appId;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

}
