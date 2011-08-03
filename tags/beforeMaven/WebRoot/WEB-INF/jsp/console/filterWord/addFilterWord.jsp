<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/include/tags.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
        <title>添加过滤词</title>
    </head>
    
    <body>
    
           添加过滤词
    
    <hr>
    <form name="searchFilterWord" action="filterWordPost.do">
    	<c:if test="${not empty param.id}">
    		<g:get var="m_word" business="filterWord" limit="id=${param.id}" />
    		<input type="hidden" name="id" value="${m_word.id}" />
    		
    		过滤词: ${m_word.word}
    		<br>
    		等级： <select class="input_title" name="level">
					<c:forEach begin="1" end="10" step="1" var="m_level">
						<c:if test="${m_level == m_word.level}">
							<option selected value="<c:out value='${m_level}' />"><c:out value='${m_level}' /></a>
						</c:if>
						<c:if test="${m_level != m_word.level}">
							<option value="<c:out value='${m_level}' />"><c:out value='${m_level}' /></a>
						</c:if>
					</c:forEach>
    			</select>
    	</c:if>
    	<c:if test="${empty param.id}">
    		<input type="hidden" name="groupId" value="${groupId}" />
    		
    		输入过滤词: <input type="text" name="word" value="" />
    		<br>
    		过滤词等级： <select class="input_title" name="level">
					<c:forEach begin="1" end="10" step="1" var="m_level">
						<c:if test="${m_level == 5}">
							<option selected value="<c:out value='${m_level}' />"><c:out value='${m_level}' /></a>
						</c:if>
						<c:if test="${m_level != 5}">
							<option value="<c:out value='${m_level}' />"><c:out value='${m_level}' /></a>
						</c:if>
					</c:forEach>
    			</select>
    	</c:if>
    	
    	<br>
    	&nbsp;&nbsp;
    	<input type="submit" value="提交">
    </form>
    
   </body>
</html>
