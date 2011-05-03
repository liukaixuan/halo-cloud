<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="org.guzz.web.context.GuzzWebApplicationContextUtil"%>
<%@page import="com.guzzservices.version.VersionControlService"%>
<%

VersionControlService s = (VersionControlService) GuzzWebApplicationContextUtil.getGuzzContext(session.getServletContext()).getService("versionControlService") ;

String path = "/fws/b3vh5xmun0r2z4pkil2g5rpxnt2mu76n0r7qqoa" ;

s.incVersion(path) ;
s.incVersion(path) ;
s.incVersion(path) ;
s.incVersion(path) ;

out.println("<p/>version is:" + s.getVersion(path)) ;

s.upgradeVersionTo(path, 100) ;
out.println("<p/>version is:" + s.getVersion(path)) ;
s.upgradeVersionTo(path, 101) ;
out.println("<p/>version is:" + s.getVersion(path)) ;
s.upgradeVersionTo(path, 102) ;
out.println("<p/>version is:" + s.getVersion(path)) ;
s.upgradeVersionTo(path, 103) ;
out.println("<p/>version is:" + s.getVersion(path)) ;

%>
