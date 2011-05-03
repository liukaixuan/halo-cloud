<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/include/tags.jsp"%>
<g:boundary>
	<g:page var="logs" business="statLog" limit="statId=${statItem.id}" orderBy="id desc" pageNo="${param.pageNo}" pageSize="20" />
</g:boundary>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
        <title>排行日志</title>
        <link type="text/css" href="../../css/smoothness/jquery-ui-1.8.9.custom.css" rel="stylesheet" />	
		<script type="text/javascript" src="../../js/jquery-1.4.4.min.js"></script>
		<script type="text/javascript" src="../../js/jquery-ui-1.8.9.custom.min.js"></script>
		
		<style type="text/css">
			/*demo page css*/
			body{ font: 62.5% "Trebuchet MS", sans-serif; margin: 5px;}
			.demoHeaders { margin-top: 2em; }
			#dialog_link {padding: .4em 1em .4em 20px;text-decoration: none;position: relative;}
			#dialog_link span.ui-icon {margin: 0 5px 0 0;position: absolute;left: .2em;top: 50%;margin-top: -8px;}
			ul#icons {margin: 0; padding: 0;}
			ul#icons li {margin: 2px; position: relative; padding: 4px 0; cursor: pointer; float: left;  list-style: none;}
			ul#icons span.ui-icon {float: left; margin: 0 4px;}
		</style>
    </head>
    
    <body>
    
    <a href="./statItemList.do?groupId=${param.groupId}">返回上一步</a>
    
    <hr>
    	最后执行错误日志：<g:out value="${statItem.errorInfo}" />
    <hr>
    
    <table border="1" width="80%">
    	<tr>
			<th>序号</th>
			<th>类型</th>
			<th>执行时间</th>
			<th>结果</th>
		</tr>
    	<c:forEach items="${logs.elements}" var="m_item">
		<tr>
			<td><c:out value="${logs.index}" /></td>
			<td><c:out value="${m_item.type}" /></td>
			<td><c:out value="${m_item.statExecuteTime}" /></td>
			<td><c:out value="${m_item.result}" /></td>
		</tr>
    	</c:forEach>
    </table>
    
    <table border="1" width="80%">
    	<tr>
			<c:import url="/WEB-INF/jsp/include/console_flip.jsp" />
		</tr>
    </table>
    
   </body>
</html>
