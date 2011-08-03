package com.guzzservices.util;

import java.io.UnsupportedEncodingException;

public class PasswordUtil {
	
	public static String md5(String psw){
		try {
			return org.apache.commons.codec.digest.DigestUtils.md5Hex(psw.getBytes("UTF-8")) ;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new RuntimeException("UTF-8", e) ;
		}
	}

}
