/**
 * FilterWordImportForm.java. created on 2006-8-8  
 */
package com.guzzservices.action.vo;

import org.springframework.web.multipart.MultipartFile;

/**
 * 
 * 
 * @author liu kaixuan
 * @date 2006-8-8 17:13:03
 */
public class FilterWordImportForm {

	private String groupId ;
	
	private MultipartFile uploadFile ;
	
	private int level ;
	
	private String color ;

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public MultipartFile getUploadFile() {
		return uploadFile;
	}

	public void setUploadFile(MultipartFile uploadFile) {
		this.uploadFile = uploadFile;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}
}
