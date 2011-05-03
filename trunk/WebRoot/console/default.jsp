<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/include/tags.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <title>服务管理台</title>
    </head>
    
    <frameset id="indexFrame" name="indexFrame" border="0" frameBorder="false" borderColor="#d0d0d0" cols=200,7,*>
    	<frame name="leftFrame" id="leftFrame" src="indexLeft.do" scrolling="auto" noresize>
		<frame name="middle" id="middle" src="switch.jsp" scrolling="no" noresize>
    	<frame name="main" id="main" src="welcome.jsp" scrolling="auto" noresize>
	</frameset>
    
</html>
