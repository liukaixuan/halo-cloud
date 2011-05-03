<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/include/tags.jsp"%>
<g:boundary>
	<g:addLimit test="${param.flag eq 'clean'}" limit="banned=fasle" />
	<g:addLimit test="${param.flag eq 'dirty'}" limit="banned=true" />
	
	<g:list var="records" business="topRecord" limit="statId=${statItem.id}" pageSize="200" orderBy="objectOrder asc" />
</g:boundary>
	
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
        <title><c:out value="${statItem.name}" />--Top内容排行榜</title>
    </head>
    
    <body>
    ( <c:out value="${statItem.name}" /> )&nbsp;
    <a href="./statTopRecordList.do?flag=all&statId=<c:out value='${statItem.id}' />">列出所有</a>
    <a href="./statTopRecordList.do?flag=clean&statId=<c:out value='${statItem.id}' />">列出将在前台显示的</a>
    <a href="./statTopRecordList.do?flag=dirty&statId=<c:out value='${statItem.id}' />">列出已经被隐藏的</a>
    &nbsp;&nbsp;||&nbsp;&nbsp;
    <a href="./statItemCommand.do?action=sort&statId=<c:out value='${statItem.id}' />">按操作次数重新排序</a>
    <a href="./statItemCommand.do?action=refresh&statId=<c:out value='${statItem.id}' />">重新统计数据</a>
    <a href="./statItemCommand.do?action=publish&statId=<c:out value='${statItem.id}' />">发布数据到前端</a>
    
    &nbsp;&nbsp;||&nbsp;
    <c:if test="${statItem.recordEditable}">
		&nbsp;<a href="./statTopRecordAction.do?statId=${statItem.id}">添加新记录</a>
	</c:if>	
    &nbsp;<a href="./statItemList.do?groupId=${statItem.groupId}">返回</a>
    
    <hr>
    
    	<script>        
        	function selectAll(isSelect){
				var nodes = recordCommandActionForm.elements;
								
				for(var i = 0 ; i < nodes.length ; i++){
					if(nodes[i].type.toLowerCase() == "checkbox"){
						nodes[i].checked = isSelect ;
					}
				}
        	}
        	
        	function batchOperation(op){
        		recordCommandActionForm.action.value = op ;
        		recordCommandActionForm.submit() ;
        	}
        	
        </script>
    
    <table border="0" width="96%">
    	<tr>
    		<td>
    			&nbsp;&nbsp;<a href="javascript:selectAll(true);">全选</a>
    			&nbsp;&nbsp;<a href="javascript:selectAll(false);">全部取消</a>
    			&nbsp;&nbsp;||&nbsp;&nbsp;<a href="javascript:batchOperation('hide');">批量隐藏</a>
    			&nbsp;&nbsp;<a href="javascript:batchOperation('show');">批量显示</a>
    		</td>
		</tr>
    </table>
     
    <form id="recordCommandActionForm" name="recordCommandActionForm" method="POST" action="statTopRecordVisibility.do">
    	<input type="hidden" name="action" id="actionFormAction">
    	<input type="hidden" name="statId" value="${statItem.id}">
      
    <table border="1" width="96%">
    	<tr>
    		<th>&nbsp;</th>
    	    <th>索引</th>
			<th>标题</th>
			<th>操作次数</th>
			<th>发布时排序位置</th>
			<th>创建时间</th>
			<th>注释1</th>
			<th>注释2</th>
			<th>注释3</th>
			<th>操作</th>
		</tr>
    	<c:forEach items="${records}" var="m_record" varStatus="m_status">
		<tr>
			<td>&nbsp;<input type="checkbox" name="ids" value="<c:out value='${m_record.id}' />" /></td>
		    <td>&nbsp;<c:out value="${m_status.index + 1}" /></td>
			<td>&nbsp;<a href='<c:out value="${m_record.objectURL}" />' target="_blank"><c:out value="${m_record.objectTitle}" escapeXml="false" /></a></td>
			<td>&nbsp;<c:out value="${m_record.opTimes}" /></td>
			<td>&nbsp;<c:out value="${m_record.objectOrder}" /></td>
			<td>&nbsp;<c:out value="${m_record.objectCreatedTime}" /></td>
			<td>&nbsp;<c:out value="${m_record.extra1}"  escapeXml="false" /></td>
			<td>&nbsp;<c:out value="${m_record.extra2}"  escapeXml="false" /></td>
			<td>&nbsp;<c:out value="${m_record.extra3}"  escapeXml="false" /></td>
			<td>
			<c:if test="${m_record.banned}">
				<a href="./statTopRecordVisibility.do?action=show&statId=${statItem.id}&id=<c:out value='${m_record.id}' />"><font color="green">显示</font></a>
			</c:if>
			<c:if test="${!m_record.banned}">
				<a href="./statTopRecordVisibility.do?action=hide&statId=${statItem.id}&id=<c:out value='${m_record.id}' />">隐藏</a>
			</c:if>
			<c:if test="${statItem.recordEditable}">
				&nbsp;<a href="./statTopRecordAction.do?statId=<c:out value='${statItem.id}' />&id=<c:out value='${m_record.id}' />">修改</a>
			</c:if>
			</td>
		</tr>
    	</c:forEach>
    </table>   
    
    </form>
    
   </body>
</html>
