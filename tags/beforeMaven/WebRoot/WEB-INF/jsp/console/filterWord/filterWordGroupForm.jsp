<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/include/tags.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>维护过滤词组</title>
</head>

<body>
    <table width="640" border="0" cellspacing="0" cellpadding="0" align="center">
      <tr>
        <td>&nbsp;</td>
      </tr>
      <!-- 输出列表 -->
              <tr><td>
			  	<form method="POST">
			  		<input type="hidden" name="groupId" value="<c:out value="${param.groupId}" default="" />" />
				  <table border="0" id="table2" cellspacing="5" cellpadding="0" width="100%">
					<tr>
						<td width="26">&nbsp;</td>
						<td width="120"><b>过滤词组名称：</b></td>
						<td align="left">
							<spring:bind path="filterWordGroupForm.filterWordGroup.name">
								<input type="text" class="input_title" id="<c:out value="${status.expression}" />" name="<c:out value="${status.expression}" />" value="<c:out value="${status.value}" />" />
								<%@ include file="/WEB-INF/jsp/include/error_inc.jsp" %>
							</spring:bind>
						</td>
						<td width="20">&nbsp;</td>
					</tr>
					<tr>
						<td width="26">&nbsp;</td>
						<td width="120"><b>标红颜色：</b></td>
						<td align="left">
							<spring:bind path="filterWordGroupForm.filterWordGroup.color">
								<input type="text" class="input_title" name="<c:out value="${status.expression}" />" id="<c:out value="${status.expression}" />" value="<c:out value="${status.value}" default="red" />" />
								<%@ include file="/WEB-INF/jsp/include/error_inc.jsp" %>
							</spring:bind>
						</td>
						<td width="20">&nbsp;</td>
					</tr>
					<tr>
						<td width="26">&nbsp;</td>
						<td width="120"><b>备注：</b></td>
						<td align="left">
							<spring:bind path="filterWordGroupForm.filterWordGroup.description">
								<input type="text" class="input_title" name="<c:out value="${status.expression}" />" id="<c:out value="${status.expression}" />" value="<c:out value="${status.value}" />" />
								<%@ include file="/WEB-INF/jsp/include/error_inc.jsp" %>
							</spring:bind>
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