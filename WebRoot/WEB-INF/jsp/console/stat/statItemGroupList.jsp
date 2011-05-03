<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/include/tags.jsp"%>
<g:boundary>
	<c:if test="${not empty groupIds}">
		<g:addInLimit name="id" value="${groupIds}" />
		<g:list var="groups" business="statItemGroup" orderBy="id asc" pageNo="1" pageSize="200" />
	</c:if>
</g:boundary>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
        <title>管理</title>
        <link type="text/css" href="../../css/smoothness/jquery-ui-1.8.9.custom.css" rel="stylesheet" />	
		<script type="text/javascript" src="../../js/jquery-1.4.4.min.js"></script>
		<script type="text/javascript" src="../../js/jquery-ui-1.8.9.custom.min.js"></script>
		<script type="text/javascript">

			$(function(){
				// Dialog
				$('#editDialog').dialog({
					autoOpen: false,
					width: 350,
					height:130,
					buttons: {
						"Ok": function() {
							var groupId = $("#ed_groupId").attr("value") ;
							var groupName = $("#ed_groupName").attr("value") ;

							if(!groupName){
								$("#errorMsg").html("请输入分组名称！") ;
							}else{

								$(this).dialog("close");
								
								$.ajax({
							   		url : "./statItemGroupAction.do" ,
							   		type: "POST",
							   		data : {"groupId" : groupId, "groupName" : groupName},
							   		success: function(map){
								   		document.location.reload() ;
							 		  }
							   	});
							}
						}, 

						"Cancel": function() {
							$(this).dialog("close"); 
						}
					}

				});

				// Dialog Link
				$('#addGroupLink').click(function(){
					$('#editDialog').dialog("option", 'title', "添加分组");
					
					$("#ed_groupId").attr("value", "0") ;
					$("#ed_groupName").attr("value", "") ;
					
					$('#editDialog').dialog('open');
					
					return false;

				});

				$('.editGroupLink').click(function(){
					$('#editDialog').dialog("option", 'title', "修改分组");
					
					$("#ed_groupId").attr("value", $(this).attr("groupId")) ;
					$("#ed_groupName").attr("value", $(this).attr("groupName")) ;
					
					$('#editDialog').dialog('open');
					
					return false;

				});
			});

		</script>

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
    
    <div id="editDialog" title="分组">
    	分组名称：<input type="text" name="groupName" id="ed_groupName" />
    	&nbsp;<span id="errorMsg" style="color:red"></span>
    	
    	<input type="hidden" name="groupId" id="ed_groupId" />
    </div>
    
    <a href="#" id="addGroupLink">添加分组（创建排行榜前，先创建排行分组）</a>
    
    <hr>
    
    <table border="1" width="96%">
    	<tr>
			<th>序号</th>
			<th>组名</th>
			<th>添加时间</th>
			<th>操作</th>
		</tr>
    	<c:forEach items="${groups}" var="m_group" varStatus="m_status">
		<tr id="sig${m_group.id}">
			<td><c:out value="${m_status.index + 1}" /></td>
			<td><a href="statItemList.do?groupId=${m_group.id}"><c:out value="${m_group.name}" /></a></td>
			<td><fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${m_group.createdTime}" /></td>
			<td>
			<a href='#' class="editGroupLink" groupId="${m_group.id}" groupName="${m_group.name}">修改</a>
			&nbsp;&nbsp;
			<a href='./bannedTopRecordList.do?groupId=${m_group.id}'>封禁记录管理</a>
			&nbsp;&nbsp;
			<a target="_blank" href='../authMembersAction.do?serviceName=gs_si&serviceKey=${m_group.id}'>权限管理</a>
			</td>
		</tr>
    	</c:forEach>
    </table>
    
   </body>
</html>
