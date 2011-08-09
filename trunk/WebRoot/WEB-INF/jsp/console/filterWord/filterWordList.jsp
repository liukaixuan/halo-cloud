<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/include/tags.jsp"%>
<g:boundary>
	<g:addLimit test="${param.level > 0}" limit="level=${param.level}" />
	<g:addLimit test="${not empty param.word}" limit="word~~%${param.word}%" />
	<g:addLimit test="${not empty param.word}" limit="word~~%${param.word}%" />
	<g:addLimit test="${not empty param.word}" limit="word~~%${param.word}%" />
	
	<g:page var="words" business="filterWord" limit="groupId=${groupId}" orderBy="id desc" pageNo="${param.pageNo}" pageSize="20" />
</g:boundary>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
        <title>过滤词管理</title>
    </head>
    
    <body>
    
    <a href="./addFilterWord.do?groupId=${groupId}">添加过滤词</a>
    &nbsp;&nbsp;
    <a target="_blank" href="../test/testWordFilter.jsp?groupId=${groupId}">测试效果</a>
    &nbsp;&nbsp;
    <a href="./filterWordReload.do?groupId=${groupId}">手动刷新加载</a>
    &nbsp;&nbsp;
    <a href="./filterWordImport.do?groupId=${groupId}">导入过滤词</a>
    &nbsp;&nbsp;
    <a href="./filterWordExport.do?groupId=${groupId}">导出过滤词</a>
    &nbsp;&nbsp;API KEY:${groupId}
    
    <hr>
    <form name="searchFilterWord">
    	<input type="hidden" name="groupId" value="${groupId}">
    	过滤词包含<input type="text" name="word" value="<c:out value='${param.word}' />">
    	过滤词等级
    	<select name="level">
    		<option value="0" >所有</option>
    		<option value="1" <c:if test="${param.level == 1}">selected</c:if>>1</option>
    		<option value="2" <c:if test="${param.level == 2}">selected</c:if>>2</option>
    		<option value="3" <c:if test="${param.level == 3}">selected</c:if>>3</option>
    		<option value="4" <c:if test="${param.level == 4}">selected</c:if>>4</option>
    		<option value="5" <c:if test="${param.level == 5}">selected</c:if>>5</option>
    		<option value="6" <c:if test="${param.level == 6}">selected</c:if>>6</option>
    		<option value="7" <c:if test="${param.level == 7}">selected</c:if>>7</option>
    		<option value="8" <c:if test="${param.level == 8}">selected</c:if>>8</option>
    		<option value="9" <c:if test="${param.level == 9}">selected</c:if>>9</option>
    		<option value="10" <c:if test="${param.level == 10}">selected</c:if>>10</option>
    	</select>
    	&nbsp;&nbsp;<input type="submit" value="查找">
    </form>
    <p>
    
    <table border="1" width="80%">
    	<tr>
			<th>序号</th>
			<th>过滤词</th>
			<th>过滤等级</th>
			<th>添加时间</th>
			<th>操作</th>
		</tr>
    	<c:forEach items="${words.elements}" var="m_word">
		<tr>
			<td><c:out value="${words.index}" /></td>
			<td><c:out value="${m_word.word}" /></td>
			<td><c:out value="${m_word.level}" /></td>
			<td><fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${m_word.createdTime}" /></td>
			<td>
			<a href='addFilterWord.do?id=${m_word.id}'>修改</a>
			&nbsp;&nbsp;
			<a href='filterWordDelete.do?id=${m_word.id}'>删除</a>
			</td>
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
