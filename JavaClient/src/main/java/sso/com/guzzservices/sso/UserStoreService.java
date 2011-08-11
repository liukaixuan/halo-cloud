/**
 * 
 */
package com.guzzservices.sso;

import java.util.Map;

import com.guzzservices.sso.stub.SSOInfo;

/**
 * 
 * 
 * 
 * @author liukaixuan(liukaixuan@gmail.com)
 */
public interface UserStoreService {

	/**
	 * Query the user info as {@link LoginUser}.
	 * 
	 * @param userName
	 * @return the {@link LoginUser} to be stored in session.
	 */
	public LoginUser getLoginUser(String userName) ;
	
	/**
	 * Can the user log in?
	 * 
	 * @param userName
	 * @param password
	 * @param IP
	 * @return {@link SSOInfo#SUCCESS} if permission granted; or return a constant error code in {@link LoginException} on failed.
	 */
	public int checkLogin(String userName, String password, String IP) ;
	
	/**
	 * Query user info.
	 * 
	 * @param userName
	 * @return return null if user not exist.
	 */
	public Map<String, Object> queryUserInfo(String userName) ;

	/**
	 * Query userId by userName.
	 * 
	 * @param userName
	 * @return return -1 if user not exist.
	 */
	public int queryUserId(String userName) ;
	
	/**
	 * Query userName by userId.
	 * 
	 * @param userId
	 * @return return null if user not exist.
	 */
	public String queryUserName(int userId) ;
	
}
