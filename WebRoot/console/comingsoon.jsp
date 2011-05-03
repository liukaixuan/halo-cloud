<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/include/tags.jsp"%>

<g:list var="configs" business="configuration" limit="groupId=${param.groupId}" pageSize="100" orderBy="createdTime asc" />

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
        <title>Service Not Available</title>
    </head>
    
    <body>
    
    Service not implemented!
    <hr/>
    Be patient please, I will write this service later.
    
   </body>
</html>
