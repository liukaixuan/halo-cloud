<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="org.guzz.web.context.GuzzWebApplicationContextUtil"%>
<%@page import="com.guzzservices.mail.SendMailService"%>
<%
SendMailService s = (SendMailService) GuzzWebApplicationContextUtil.getGuzzContext(session.getServletContext()).getService("sendMailService") ;

boolean result = s.sendPlainMail("halo-cloud@guzzservices.com", "liukaixuan@gmail.com", "test plain subject", "test plain content!") ;

out.println("<p/>result:" + result) ;

result = s.sendHtmlMail("halo-cloud@guzzservices.com", "liukaixuan@gmail.com", "test html subject", "test <font color='red'>HTML</font> <p/> content!") ;

out.println("<p/>result:" + result) ;


%>
