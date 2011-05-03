/**
 * 
 */
package com.guzzservices.manager;

import java.util.List;

import org.guzz.transaction.WriteTranSession;

import com.guzzservices.manager.impl.Members;

/**
 * 
 * 
 * 
 * @author liukaixuan(liukaixuan@gmail.com)
 */
public interface IAuthManager {
	
	/**
	 * 新项目创建时，初始化权限
	 */
	public void newAuth(WriteTranSession write, String userName, String serviceName, String serviceKey) ;
	
	public Members getAuthedMembers(String serviceName, String serviceKey);
	
	/**
	 * 保存权限表
	 */
	public void storeAuthedMembers(String serviceName, String serviceKey, Members m);
	
	public List<String> listAuthedServiceKeys(String userName, String serviceName) ;
	
}
