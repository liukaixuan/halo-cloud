/**
 * 
 */
package com.guzzservices.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.support.WebApplicationContextUtils;

import com.guzzservices.manager.ISessionManager;
import com.guzzservices.sso.LoginUser;

/**
 * 
 * filter for users.
 *
 * @author liukaixuan(liukaixuan@gmail.com)
 */
public class ConsoleFilter implements javax.servlet.Filter{
	
	private ISessionManager sessionManager ;

	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request ;
		HttpServletResponse res = (HttpServletResponse) response ;
				
		LoginUser loginUser = sessionManager.getLoginUser(req, res) ;
		
		if(loginUser.isLogin()){
			chain.doFilter(request, response) ;
		}else{
			res.sendRedirect("../login.jsp") ;
		}
	}

	public void init(FilterConfig filterConfig) throws ServletException {
		this.sessionManager =  (ISessionManager) WebApplicationContextUtils.getWebApplicationContext(filterConfig.getServletContext()).getBean("sessionManager") ;
	}

}
