<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/include/tags.jsp"%>

<g:list var="configs" business="configuration" limit="groupId=${param.groupId}" pageSize="100" orderBy="createdTime asc" />

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
        <title>IPService</title>
    </head>
    
    <body>
    
    IP Service. 
    <hr/>
    Read the documentation to use this service directly. You don't have to configure anything more.
    <p>
    Documentation: <a href="http://www.guzzservices.com/2010/man_ip_service/" target="_blank">http://www.guzzservices.com/2010/man_ip_service/</a>
    </p>
    
   </body>
</html>
