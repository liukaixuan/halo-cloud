<%@ page language="java" pageEncoding="UTF-8"%>

<%@ include file="/WEB-INF/jsp/include/tags.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <script language="JavaScript" src="<c:out value="${htmlTree.file}" escapeXml="false"/>"></script>
        <c:out value="${htmlTree.items}" escapeXml="false"/>
        <title>系统配置</title>
        <style>
        	body,.con{
        		background:#6699cc;
        	}
        	a:link{
        		color:#ffffff;
        		text-decoration:none;
        	}
        	a:visited{
        		color:#ffffff;
        		text-decoration:none;
        	}
        	a:hover{
        		color:#cc3300;
        		text-decoration:none;
        	}
        	a:active{
        		color:#ff0000;
        		text-decoration:none;
        	}
        </style>
    </head>
    
    <body>
	<div id="con">
   		<c:out value="${htmlTree.show}" escapeXml="false"/>
    </div>
   </body>
</html>
