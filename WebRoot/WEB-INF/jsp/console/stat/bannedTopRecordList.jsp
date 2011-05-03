<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/include/tags.jsp"%>

<g:get var="statItemGroup" limit="id=${groupId}" business="statItemGroup" />
<g:page var="records" business="bannedTopRecord" limit="groupId=${statItemGroup.id}" pageSize="50" />
	
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
        <title><c:out value="${statItemGroup.name}" />--封禁记录</title>
		<script type="text/javascript" src="../../js/jquery-1.4.4.min.js"></script>
		<script type="text/javascript">
			$(document).ready(function(){
				$("a[rid]").bind('click', function(event){
					event.preventDefault() ;

					var id = "#tr" + $(this).attr("rid") ;

					$.ajax({
				   		url : $(this).attr("href") ,
				   		trid : id,
				   		success: function(text){
				   			$(this.trid).remove() ;
				   		}
					});
				}) ;
			});

		</script>
    </head>
    
    <body>
    ( <c:out value="${statItemGroup.name}" /> )&nbsp;&nbsp;<a href="statItemGroupList.do">返回</a>
    
    <hr>
    
    <table border="1" width="96%">
    	<tr>
    	    <th>索引</th>
			<th>id</th>
			<th>标题</th>
			<th>创建时间</th>
			<th>最后命中时间</th>
			<th>操作</th>
		</tr>
    	<c:forEach items="${records.elements}" var="m_record">
		<tr id="tr${m_record.id}">
		    <td>&nbsp;${records.index}</td>
		    <td>&nbsp;<c:out value="${m_record.objectId}" /></td>
			<td>&nbsp;<a href='<c:out value="${m_record.objectURL}" />' target="_blank"><c:out value="${m_record.objectTitle}" escapeXml="false" /></a></td>
			<td>&nbsp;<fmt:formatDate value="${m_record.createdTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
			<td>&nbsp;<fmt:formatDate value="${m_record.lastHitTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
			<td>
				<a rid="${m_record.id}" href="./bannedTopRecordDelete.do?id=<c:out value='${m_record.id}' />">删除</a>
			</td>
		</tr>
    	</c:forEach>
    </table>
    
    <table border="1" width="96%">
    	<tr>
			<c:import url="/WEB-INF/jsp/include/console_flip.jsp" />
		</tr>
    </table>
    
   </body>
</html>
