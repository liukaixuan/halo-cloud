<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/include/tags.jsp"%>
<%@page import="org.guzz.web.context.GuzzWebApplicationContextUtil"%>
<%@page import="com.guzzservices.management.*"%>
<%
AppLogService appLogService = (AppLogService) GuzzWebApplicationContextUtil.getGuzzContext(session.getServletContext()).getService("appLogService") ;

java.util.HashMap<String, Object> props = new java.util.HashMap<String, Object>() ;
props.put("userNick", "管理员A") ;
props.put("postId", System.currentTimeMillis()) ;
props.put("title", "动车太可恶了！") ;

appLogService.insertLog(12345, props) ;

java.util.LinkedList<String> cs = new java.util.LinkedList<String>() ;
cs.add("userId=12345") ;

org.guzz.dao.PageFlip data = appLogService.queryLogs(cs, "id asc", 1, 20) ;
request.setAttribute("data", data) ;

out.println("<hr/>") ;

%>
<table border="1" width="96%">
    	<tr>
			<th>序号</th>
			<th>userNick</th>
			<th>postId</th>
			<th>title</th>
			<th>createdTime</th>
		</tr>
    	<c:forEach items="${data.elements}" var="m_log">
		<tr>
			<td><c:out value="${data.index}" /></td>
			<td><c:out value="${m_log.otherProps.userNick}" /></td>
			<td><c:out value="${m_log.otherProps.postId}" /></td>
			<td><c:out value="${m_log.otherProps.title}" /></td>
			<td><c:out value="${m_log.createdTime}" /></td>
		</tr>
    	</c:forEach>
</table>

