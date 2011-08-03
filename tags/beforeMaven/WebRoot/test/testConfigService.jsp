<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.guzz.web.context.GuzzWebApplicationContextUtil"%>
<%@page import="org.guzz.util.RequestUtil"%>
<%@page import="com.guzzservices.dir.IPLocateService"%>
<%@page import="com.guzzservices.dir.ip.LocateResult"%>
<%@page import="com.guzzservices.management.ConfigurationService"%>
<%

int count = RequestUtil.getParameterAsInt(request, "loop", 1) ;

ConfigurationService s = (ConfigurationService) GuzzWebApplicationContextUtil.getGuzzContext(session.getServletContext()).getService("configurationService") ;

long begin = System.currentTimeMillis() ;

for(int i = 0 ; i < count ; i++){
	if(count == 1){
		out.println("<br>评论审核策略:" + s.getInt("comments.check.policy", 1)) ;
		out.println("<br>邮件服务器IP:" + s.getString("mail.server.ip")) ;
		out.println("<br>页面banner头代码:" + s.getString("page.head.banner")) ;
	}
}

long end = System.currentTimeMillis() ;

out.println("<p/>timeUsed:" + (end - begin) + "ms, loop:" + count) ;

%>
