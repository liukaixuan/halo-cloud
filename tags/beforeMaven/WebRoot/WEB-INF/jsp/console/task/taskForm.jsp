<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/include/tags.jsp"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>设置任务</title>
</head>

<body>
    <table width="96%" border="0" cellspacing="0" cellpadding="0" align="center">
      <tr>
        <td>&nbsp;</td>
      </tr>
      <!-- 输出列表 -->
              <tr><td>
			  	<form id="taskForm" name="taskForm" method="POST">
			  		<input type="hidden" name="id" value="<c:out value="${param.id}" default="-1" />" />
			  		<input type="hidden" name="groupId" value="<c:out value="${param.groupId}" default="-1" />" />
				  <table border="0" id="table2" cellspacing="5" cellpadding="0" width="100%">
					<tr>
						<td>&nbsp;</td>
						<td><b>名称：</b></td>
						<td align="left">
							<spring:bind path="taskForm.task.name">
								<input size="50" type="text" class="input_title" id="<c:out value="${status.expression}" />" name="<c:out value="${status.expression}" />" value="<c:out value="${status.value}" />" />
								<%@ include file="/WEB-INF/jsp/include/error_inc.jsp" %>
							</spring:bind>
						</td>
						<td width="20">&nbsp;</td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td><b>quartz cron表达式：</b></td>
						<td align="left">
							<spring:bind path="taskForm.task.cronExpression">
								<input size="50" <c:if test="${!taskForm.newTask}">readonly</c:if> type="text" class="input_title" name="<c:out value="${status.expression}" />" id="<c:out value="${status.expression}" />" value="<c:out value="${status.value}" />" />
								<br/>分钟（0~59），小时（0~23），天（月）（0~31，但是你需要考虑你月的天数），月（0~11），天（星期）（1~7 1=SUN 或 SUN，MON，TUE，WED，THU，FRI，SAT），年份（1970－2099）
								<%@ include file="/WEB-INF/jsp/include/error_inc.jsp" %>
							</spring:bind>
						</td>
						<td width="20">&nbsp;</td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td><b>任务请求URL：</b></td>
						<td align="left">
							<spring:bind path="taskForm.task.remoteUrl">
								<input type="text" size="50" class="input_title" name="<c:out value="${status.expression}" />" id="<c:out value="${status.expression}" />" value="<c:out value="${status.value}" />" />
								<br/>定时请求的任务地址
								<%@ include file="/WEB-INF/jsp/include/error_inc.jsp" %>
							</spring:bind>
						</td>
						<td width="20">&nbsp;</td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td><b>authKey：</b></td>
						<td align="left">
							<spring:bind path="taskForm.task.authKey">
								<input type="text" size="50" class="input_title" name="<c:out value="${status.expression}" />" id="<c:out value="${status.expression}" />" value="<c:out value="${status.value}" />" />
								<br/>附加到任务请求地址的参数值，参数名为"authKey"。用于任务执行者判断调用的合法性。
								<%@ include file="/WEB-INF/jsp/include/error_inc.jsp" %>
							</spring:bind>&nbsp;
						</td>
						<td width="20">&nbsp;</td>
					</tr>
				  </table>		  			
				  
				   <table border="0" id="table2" cellspacing="5" cellpadding="0" width="100%">					
					<tr>
						<td width="125">&nbsp;</td>
						<td width="200"> 
				 		<input type="submit" name="submit" value="提交"/>  
				 		<input type="reset" class="smb_btn" name="submit" value="取消" />
						</td>
						<td>
							&nbsp;
						</td>
						<td width="20">&nbsp;</td>
					</tr>
				  </table>
				  
				  </form>
				</td></tr>
              <!-- /输出列表 -->              
              <tr>
                <td height="5"></td>
              </tr>
</table>

</body>
</html>