<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/include/tags.jsp"%>
<g:boundary>
	<g:addLimit limit="appId=${appId}" />
	<g:list var="items" business="logCustomProperty" orderBy="id asc" pageSize="500" />
</g:boundary>

<g:get var="logApp" business="logApp" limit="id=${appId}" />
	
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
        <title>自定义属性</title>
		<style type="text/css">
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
    
    <a href="./logCustomPropertyAction.do?appId=${appId}">新建自定义属性</a>
    &nbsp;&nbsp;||&nbsp;&nbsp;
    <a href="./logAppReload.do?appId=${appId}">应用修改生效（应用前请调整相关的表结构）</a>
    &nbsp;&nbsp;||&nbsp;&nbsp;
    接入secureCode: ${logApp.secureCode}
    &nbsp;&nbsp;||&nbsp;&nbsp;
    <a href="logAppList.do">返回日志应用列表</a>
    
    <hr>
	
    <table border="1" width="80%">
    	<tr>
			<th>序号</th>
			<th>Java中属性名</th>
			<th>DB中的字段名</th>
			<th>显示名称</th>
			<th>数据类型</th>
			<th>操作</th>
		</tr>
    	<c:forEach items="${items}" var="m_item" varStatus="m_status">
		<tr>
			<td><c:out value="${m_status.index + 1}" /></td>
			<td><c:out value="${m_item.propName}" /></td>
			<td><c:out value="${m_item.colName}" /></td>
			<td><c:out value="${m_item.displayName}" /></td>
			<td><c:out value="${m_item.dataType}" /></td>
			<td>
			<a href='./logCustomPropertyAction.do?id=${m_item.id}'>修改</a>
			&nbsp;&nbsp;
			<a href='./logCustomPropertyDelete.do?id=${m_item.id}'>删除</a>
			&nbsp;&nbsp;
			</td>
		</tr>
    	</c:forEach>
    </table>
    
   </body>
</html>
