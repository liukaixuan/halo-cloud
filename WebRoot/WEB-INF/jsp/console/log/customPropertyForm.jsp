<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/include/tags.jsp"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>设置日志自定义属性</title>
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
			  		<input type="hidden" name="appId" value="<c:out value="${param.appId}" default="-1" />" />
	
				  <table border="0" id="table2" cellspacing="5" cellpadding="0" width="100%">
					<tr>
						<td>&nbsp;</td>
						<td><b>Java中使用的属性名propName：</b></td>
						<td align="left">
							<spring:bind path="logCustomPropertyForm.property.propName">
								<input size="50" maxlength="32" type="text" class="input_title" id="<c:out value="${status.expression}" />" name="<c:out value="${status.expression}" />" value="<c:out value="${status.value}" />" />
								<br/>只允许英文和数字
								<%@ include file="/WEB-INF/jsp/include/error_inc.jsp" %>
							</spring:bind>
						</td>
						<td width="20">&nbsp;</td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td><b>在数据库中存储的字段名colName：</b></td>
						<td align="left">
							<spring:bind path="logCustomPropertyForm.property.colName">
								<input size="50" maxlength="32" type="text" class="input_title" name="<c:out value="${status.expression}" />" id="<c:out value="${status.expression}" />" value="<c:out value="${status.value}" />" />
								<br/>只允许英文和数字
								<%@ include file="/WEB-INF/jsp/include/error_inc.jsp" %>
							</spring:bind>
						</td>
						<td width="20">&nbsp;</td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td><b>对普通用户显示的名称displayName：</b></td>
						<td align="left">
							<spring:bind path="logCustomPropertyForm.property.displayName">
								<input type="text" maxlength="32" size="50" class="input_title" name="<c:out value="${status.expression}" />" id="<c:out value="${status.expression}" />" value="<c:out value="${status.value}" />" />
								<br/>可以为中文
								<%@ include file="/WEB-INF/jsp/include/error_inc.jsp" %>
							</spring:bind>
						</td>
						<td width="20">&nbsp;</td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td><b>dataType：</b></td>
						<td align="left">
							<spring:bind path="logCustomPropertyForm.property.dataType">
								<input type="text" maxlength="32" size="50" class="input_title" name="<c:out value="${status.expression}" />" id="<c:out value="${status.expression}" />" value="<c:out value="${status.value}" />" />
								<br/>类似hbm.xml中的type属性，如：string, int, bigint等。
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