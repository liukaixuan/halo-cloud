/**
 * 
 */
package com.guzzservices.manager;

import com.guzzservices.business.TaskGroup;
import com.guzzservices.sso.LoginUser;

/**
 * 
 * 
 * 
 * @author liukaixuan(liukaixuan@gmail.com)
 */
public interface ITaskGroupManager extends IBaseManager<TaskGroup> {

	public void add(LoginUser loginUser, TaskGroup group) ;
	

}
