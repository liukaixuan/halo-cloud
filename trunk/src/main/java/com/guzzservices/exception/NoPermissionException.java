/**
 * 
 */
package com.guzzservices.exception;

import org.guzz.exception.GuzzException;

/**
 * 
 * 
 * 
 * @author liukaixuan(liukaixuan@gmail.com)
 */
public class NoPermissionException extends GuzzException {
	
	public NoPermissionException(){
		super() ;
	}
	
	public NoPermissionException(String msg){
		super(msg) ;
	}

}
