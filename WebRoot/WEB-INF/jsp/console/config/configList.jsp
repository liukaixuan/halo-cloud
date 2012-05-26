<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/include/tags.jsp"%>

<g:list var="configs" business="configuration" limit="groupId=${groupId}" pageSize="200" orderBy="createdTime asc" />

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
        <title>系统配置</title>
    </head>
    
    <body>
    <a href="./configPost.do?groupId=${groupId}">添加配置项</a>
    &nbsp;&nbsp;<a href="./configImport.do?groupId=${groupId}">导入配置项</a>
    &nbsp;&nbsp;<a href="./configExport.do?groupId=${groupId}">导出配置项</a>
    &nbsp;&nbsp;API KEY:${groupId}
    <hr>
    
    <form name="batchConfig" method="POST" action="configBatchUpdate.do">
    	<input type="hidden" name="groupId" value="${groupId}" />
    	
    <table border="1" width="96%">
    	<tr>
    	    <th>所在项</th>
			<th>参数值</th>
			<th>说明</th>
			<th>单独修改</th>
		</tr>
    	<c:forEach items="${configs}" var="m_config">
		<tr>
		    <td><c:out value="${m_config.name}" default="${m_config.parameter}" /></td>
			<td>
				<c:if test="${m_config.enumValues}">
					<select name='config.<c:out value="${m_config.parameter}" />'>
						<c:forEach items="${m_config.validValuesArray}" var="m_value">
							<c:if test="${m_value == m_config.value}">
								<option selected value='<c:out value="${m_value}" />'>${m_value}</option> 
							</c:if>
							<c:if test="${m_value != m_config.value}">
								<option value='<c:out value="${m_value}" />'>${m_value}</option> 
							</c:if>
						</c:forEach>
					</select>
				</c:if>
				
				<c:if test="${!m_config.enumValues}">
					<c:if test="${'text' eq m_config.type }">
						<textarea rows="6" cols="30" name='<c:out value="config.${m_config.parameter}" />' ><c:out value="${m_config.value}" /></textarea>
					</c:if>
					
					<c:if test="${'text' ne m_config.type }">
						<input type="text" size="38" name='<c:out value="config.${m_config.parameter}" />' value='<c:out value="${m_config.value}" />'>
					</c:if>
				</c:if>
			</td>
			<td><c:out value="${m_config.description}" default="N/A" /></td>
			<td><a href="configPost.do?id=${m_config.id}&groupId=${m_config.groupId}">修改</a></td>
		</tr>
    	</c:forEach>
    </table>
    
    <table border="1" width="96%">
    	<tr>
			<td align="center">
				<input type="submit" value="保存设置">&nbsp;&nbsp;&nbsp;
				<input type="reset" value="取消修改">
			</td>
		</tr>
    </table>
    
    </form>
    
   </body>
</html>
