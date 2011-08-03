<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/include/tags.jsp"%>
<g:boundary>
	<c:if test="${not empty groupIds}">
		<g:addInLimit name="id" value="${groupIds}" />
		<g:list var="groups" business="configurationGroup" orderBy="createdTime asc" pageNo="1" pageSize="200" />
	</c:if>
</g:boundary>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
        <title>系统配置管理</title>
    </head>
    
    <body>
   		<a href="./configGroupPost.do">创建新组</a>
	    <hr>
	    <p>
    
	    <table border="1" width="80%">
	    	<tr>
				<th>序号</th>
				<th>组名称</th>
				<th>添加时间</th>
				<th>操作</th>
			</tr>
	    	<c:forEach items="${groups}" var="m_group" varStatus="m_status">
			<tr>
				<td><c:out value="${m_status.index + 1}" /></td>
				<td><a href="configList.do?groupId=${m_group.id}"><c:out value="${m_group.name}" /></a></td>
				<td><fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${m_group.createdTime}" /></td>
				<td>
				<a href="configGroupPost.do?groupId=${m_group.id}">修改</a>
				&nbsp;&nbsp;
				<a target="_blank" href='./authMembersAction.do?serviceName=gs_config&serviceKey=${m_group.id}'>权限管理</a>
				</td>
			</tr>
	    	</c:forEach>
	    </table>
   </body>
</html>
