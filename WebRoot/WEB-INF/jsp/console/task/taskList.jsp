<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/include/tags.jsp"%>
<g:boundary>
	<g:addLimit limit="groupId=${groupId}" />
	<g:page var="items" business="task" orderBy="id asc" pageNo="${param.pageNo}" pageSize="20" />
</g:boundary>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
        <title>管理任务</title>
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
							var id = $("#ed_id").attr("value") ;
							var cron = $("#ed_cron").attr("value") ;

							if(!cron){
								$("#errorMsg").html("请输入cron！") ;
							}else{

								$(this).dialog("close");
								
								$.ajax({
							   		url : "./taskReschedule.do" ,
							   		type: "POST",
							   		data : {"id" : id, "cron" : cron},
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

				$('.editGroupLink').click(function(){
					$('#editDialog').dialog("option", 'title', "修改cron表达式");
					
					$("#ed_id").attr("value", $(this).attr("id")) ;
					$("#ed_cron").attr("value", $(this).attr("cron")) ;
					
					$('#editDialog').dialog('open');
					
					return false;
				});
			});

		</script>

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
    
    <div id="editDialog" title="调整任务执行计划">
    	新cron表达式：<input type="text" name="cron" id="ed_cron" />
    	&nbsp;<span id="errorMsg" style="color:red"></span>
    	
    	<input type="hidden" name="id" id="ed_id" />
    </div>
    
    <a href="./taskAction.do?groupId=${groupId}">创建任务</a>
    &nbsp;&nbsp;||&nbsp;&nbsp;
    <a href="taskGroupList.do">返回组管理</a>
    
    <hr>
    
    <table border="1" width="80%">
    	<tr>
			<th>序号</th>
			<th>名称</th>
			<td>返回值</td>
			<th>cron表</th>
			<th>操作</th>
		</tr>
    	<c:forEach items="${items.elements}" var="m_item">
		<tr>
			<td><c:out value="${items.index}" /></td>
			<td><c:out value="${m_item.name}" /></td>
			<td><c:out value="${m_item.errorCode}" /></td>
			<td><c:out value="${m_item.cronExpression}" /></td>
			<td>
			<a href='#' class="editGroupLink" id="${m_item.id}" cron="${m_item.cronExpression}">调整执行计划</a>
			&nbsp;&nbsp;
			<a href='./taskAction.do?id=${m_item.id}'>修改任务</a>
			&nbsp;&nbsp;
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
