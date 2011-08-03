/**
 * 
 */
package com.guzzservices.sso;

/**
 * 
 * 
 * 
 * @author liukaixuan(liukaixuan@gmail.com)
 */
public class LoginException extends Exception {
	
	public static final int NO_SUCH_USER = 1 ;
	
	public static final int PASSWORD_WRONG = 5 ;
	
	public static final int IP_BANNED = 10 ;
	
	public static final int USER_BANNED = 15 ;
	
	public static final int PERMISSION_DENIED = 20 ;
	
	public static final int SERVER_INTERNAL_ERROR = 30 ;
	
	private int errorCode ;
	
	private String errorMsg ;
	
	public LoginException(int errorCode, String errorMsg){
		super("error code:" + errorCode) ;
	
		this.errorCode = errorCode ;
		this.errorMsg = errorMsg ;
	}

	public LoginException(int errorCode) {
		this.errorCode = errorCode ;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

}
