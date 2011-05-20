/**
 * 
 */
package com.guzzservices.sso.impl;

import java.io.IOException;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.guzz.service.AbstractService;
import org.guzz.service.ServiceConfig;
import org.guzz.util.CookieUtil;
import org.guzz.util.StringUtil;

import com.guzzservices.rpc.util.JsonUtil;
import com.guzzservices.sso.GuestUser;
import com.guzzservices.sso.LocalSessionService;
import com.guzzservices.sso.LoginException;
import com.guzzservices.sso.LoginUser;
import com.guzzservices.sso.SSOService;
import com.guzzservices.sso.stub.CookieInfo;
import com.guzzservices.sso.stub.SSOInfo;

/**
 * 
 * 
 * 
 * @author liukaixuan(liukaixuan@gmail.com)
 */
public abstract class AbstractSSOServiceImpl extends AbstractService implements SSOService {
	
	private String sessionIdCookieName = "guzz_session_id" ;
	
	private String sessionUserCookieName = "guzz_session_user" ;
	
	protected CookieUtil cookieUtil ;
	
	private LocalSessionService localSessionService ;

	public boolean configure(ServiceConfig[] scs) {
		if(scs.length > 0){
			Properties props = scs[0].getProps() ;
			
			cookieUtil = CookieUtil.forVersion0() ;
			cookieUtil.setDomain(props.getProperty("cookieDomain")) ;
			this.sessionIdCookieName = props.getProperty("sessionIdCookieName", sessionIdCookieName) ;
			this.sessionUserCookieName = props.getProperty("sessionUserCookieName", sessionUserCookieName) ;
			
			return true ;
		}else{
			cookieUtil = CookieUtil.forVersion0() ;
		}
		
		return true ;
	}

	public boolean isAvailable() {
		return true ;
	}

	public void shutdown() {}

	public void startup() {}
	
	public String readSessionId(HttpServletRequest request, HttpServletResponse response){
		String sid = cookieUtil.readCookie(request, sessionIdCookieName) ;
		
		if(StringUtil.isEmpty(sid)){
			sid = request.getParameter(sessionIdCookieName) ;
		}
		
		if(StringUtil.isEmpty(sid)){
			sid = null ;
		}
		
		return sid ;
	}
	
	public CookieUser readCookieUser(HttpServletRequest request, HttpServletResponse response) throws IOException{
		String hexValue = cookieUtil.readCookie(request, sessionUserCookieName) ;
		
		if(StringUtil.isEmpty(hexValue)){
			hexValue = request.getParameter(sessionUserCookieName) ;
		}
		
		//no cookie
		if(StringUtil.isEmpty(hexValue)){
			return null ;
		}
		
		String cookieValue = null ;
		
		try {
			cookieValue = new String(Hex.decodeHex(hexValue.toCharArray()), "UTF-8");
		} catch (DecoderException e) {
			log.error(hexValue, e) ;
			
			throw new IOException("decode error:" + hexValue) ;
		}
		
		return JsonUtil.fromJson(cookieValue, CookieUser.class) ;
	}
	
	public boolean isLogin(HttpServletRequest request, HttpServletResponse response){
		return readSessionId(request, response) != null ;
	}

	public void login(HttpServletRequest request, HttpServletResponse response, String userName) throws LoginException {
		login(request, response, userName, CookieUtil.COOKIE_AGE_SESSION) ;
	}

	public void login(HttpServletRequest request, HttpServletResponse response, String userName, int maxAge) throws LoginException {
		login(request, response, userName, null, maxAge, false) ;
	}

	public void login(HttpServletRequest request, HttpServletResponse response, String userName, String password) throws LoginException {
		login(request, response, userName, password, CookieUtil.COOKIE_AGE_SESSION) ;
	}

	public void login(HttpServletRequest request, HttpServletResponse response, String userName, String password, int maxAge) throws LoginException {
		login(request, response, userName, password, maxAge, true) ;
	}
	
	public Object getLoginUser(HttpServletRequest request, HttpServletResponse response) {
		Object cachedUser = request.getAttribute(CACHED_LOGIN_USER_KEY) ;
		
		if(cachedUser != null) return cachedUser ;
		
		//First, read from cookie to check out whether the user is logged in.
		try {
			CookieUser cu = this.readCookieUser(request, response) ;
			
			if(cu != null && cu.isLogin()){
				//user is logged in. Load it from server-side.		
				String sessionId = this.readSessionId(request, response) ;
				
				LoginUser lu = null ;
				
				if(sessionId != null){
					lu = internalGetLoginUser(request, response, cu, sessionId) ;
				}
				
				if(lu != null && lu.isLogin() && localSessionService != null){
					cachedUser = localSessionService.getLocalLoginUser(request, response, sessionId, lu) ;
				}else if(localSessionService != null){
					cachedUser = localSessionService.getLocalGuest(request, response) ;
				}else{
					cachedUser = lu ;
				}
			}
		} catch (Exception e) {
			log.error("fail to read user info from cookie.", e) ;
		}
		
		if(cachedUser == null){
			cachedUser = localSessionService != null ? localSessionService.getLocalGuest(request, response) : GuestUser.GUEST ;
		}
		
		//cache to the request
		request.setAttribute(CACHED_LOGIN_USER_KEY, cachedUser) ;
		
		return cachedUser ;
	}

	public void logout(HttpServletRequest request, HttpServletResponse response) {
		String sessionId = this.readSessionId(request, response) ;
		
		SSOInfo info = internalLogout(request, response, sessionId) ;
			
		if(info != null && info.isSuccess()){
			//write cookies.
			for(CookieInfo c : info.getCookieInfos()){
				cookieUtil.deleteCookie(response, c.getName(), c.getDomain(), "/") ;
			}
		}
		
		//local logout on this machine
		if(localSessionService != null){
			localSessionService.localLogout(request, response, sessionId) ;
		}
		
		request.removeAttribute(CACHED_LOGIN_USER_KEY) ;
	}
	
	protected void login(HttpServletRequest request, HttpServletResponse response, String userName, String password, int maxAge, boolean checkPassword) throws LoginException {
		String sessionId = this.readSessionId(request, response) ;
		
		//local logout on this machine
		if(localSessionService != null && sessionId != null){
			localSessionService.localLogout(request, response, sessionId) ;
		}
		
		SSOInfo info = internalLogin(request, response, sessionId, userName, password, maxAge, checkPassword) ;
		
		if(info != null){
			if(info.isSuccess()){
				//TODO: p3p?
				//response.setHeader("P3P","CP=\"NON DSP COR CURa ADMa DEVa TAIa PSAa PSDa IVAa IVDa CONa HISa TELa OTPa OUR UNRa IND UNI COM NAV INT DEM CNT PRE LOC\"") ;
				
				//write cookies.
				for(CookieInfo c : info.getCookieInfos()){
					cookieUtil.writeCookie(response, c.getName(), c.getValue(), c.getDomain(), "/", c.getMaxAge()) ;
					
					//把sessionId保存到request中，使得此方法成功后，可以直接读取到登录的用？？？是否有价值继续看反馈吧。
				}
			}else{
				throw new LoginException(info.getErrorCode()) ;
			}
		}else{
			throw new LoginException(LoginException.SERVER_INTERNAL_ERROR) ;
		}
		
		request.removeAttribute(CACHED_LOGIN_USER_KEY) ;
	}
	
	protected abstract SSOInfo internalLogout(HttpServletRequest request, HttpServletResponse response, String sessionId) ;
	
	protected abstract LoginUser internalGetLoginUser(HttpServletRequest request, HttpServletResponse response, CookieUser cu, String sessionId) ;
	
	protected abstract SSOInfo internalLogin(HttpServletRequest request, HttpServletResponse response, String oldSessionId, String userName, String password, int maxAge, boolean checkPassword) throws LoginException;

	public void setLocalSessionService(LocalSessionService localSessionService) {
		this.localSessionService = localSessionService;
	}
	
}

