<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/include/tags.jsp"%>
<g:boundary>
	<c:if test="${not empty appIds}">
		<g:addInLimit name="id" value="${appIds}" />
		<g:list var="groups" business="logApp" orderBy="id asc" pageNo="1" pageSize="200" />
	</c:if>
</g:boundary>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <title>日志列表</title>
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
    
    <hr>
    	<form>
    		<input name="appId" value="${appId}" type="hidden" />
    		
    		用户编号：<input name="userId" type="input" value="${param.userId}" />
    		开始时间：<input name="startTime" type="input" value="${param.startTime}" />
    		结束时间：<input name="endTime" type="input" value="${param.endTime}" />
    		
    		&nbsp;&nbsp;<input type="submit" value="检索" />
    	</form>
    <hr>
    
    <c:if test="${not empty logs}">
    <table border="1" width="96%">
		<tr>
			<th>序号</th>
			<th>用户编号</th>
			<c:forEach items="${customPropNames}" var="m_propName">
			<th>${customProperties[m_propName]}</th>
			</c:forEach>
			<th>记录时间</th>
		</tr>
    	<c:forEach items="${logs.elements}" var="m_log">
		<tr>
			<td><c:out value="${logs.index}" /></td>
			<td><c:out value="${m_log.userId}" /></td>
			
			<c:forEach items="${customPropNames}" var="m_propName">
			<td><c:out value="${m_log.otherProps[m_propName]}" /></td>
			</c:forEach>
			
			<td><fmt:formatDate value="${m_log.createdTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
		</tr>
    	</c:forEach>
    </table>
    
    <table border="1" width="96%">
    	<tr>
			<c:import url="/WEB-INF/jsp/include/console_flip.jsp" />
		</tr>
    </table>
    </c:if>
    
   </body>
</html>
