/**
 * FilterWordForm.java. created on 2006-3-10  
 */
package com.guzzservices.action.vo;

import com.guzzservices.business.FilterWordGroup;


/**
 * FilterWord 的提交表单。
 * 
 * @action FilterWordAction
 */
public class FilterWordGroupForm {

	private FilterWordGroup filterWordGroup ;
	
	private boolean newGroup ;
	
	public FilterWordGroupForm(FilterWordGroup filterWordGroup){
		if(filterWordGroup != null){
			this.filterWordGroup = filterWordGroup ;
			this.newGroup = false ;
		}else{
			this.filterWordGroup = new FilterWordGroup() ;
			this.newGroup = true ;
		}
	}

	public FilterWordGroup getFilterWordGroup() {
		return filterWordGroup;
	}

	public void setFilterWordGroup(FilterWordGroup filterWordGroup) {
		this.filterWordGroup = filterWordGroup;
	}

	public boolean isNewGroup() {
		return newGroup;
	}

	public void setNewGroup(boolean newGroup) {
		this.newGroup = newGroup;
	}
	
}
