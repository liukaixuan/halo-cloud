<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/include/tags.jsp"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>服务授权</title>
</head>

<body>
<c:if test="${isOwner}">
    <table width="96%" border="0" cellspacing="0" cellpadding="0" align="center">
      <tr height="20px;">
        <td>&nbsp;<b>服务授权：</b></td>
      </tr>
      <!-- 输出列表 -->
              <tr><td>
			  	<form method="POST">
			  		<input type="hidden" name="serviceName" value="${param.serviceName}" />
			  		<input type="hidden" name="serviceKey" value="${param.serviceKey}" />
				  <table border="0" id="table2" cellspacing="5" cellpadding="0" width="100%">
					<tr>
						<td width="10px;">&nbsp;</td>
						<td align="left" width="400px;"><b>拥有者（可以继续给其他人授权）：</b><br/>
							<spring:bind path="membersForm.owners">
								<textarea rows="10" cols="50" id="${status.expression}" name="${status.expression}">${status.value}</textarea>
								<br/>
								<%@ include file="/WEB-INF/jsp/include/error_inc.jsp" %>
							</spring:bind>
						</td>
						<td>&nbsp;每行1个用户登录名/邮箱</td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td><b>贡献者：</b><br />
							<spring:bind path="membersForm.commiters">
								<textarea rows="10" cols="50" id="${status.expression}" name="${status.expression}">${status.value}</textarea>
								<br/>
								<%@ include file="/WEB-INF/jsp/include/error_inc.jsp" %>
							</spring:bind>
						</td>
						<td>&nbsp;每行1个用户登录名/邮箱</td>
					</tr>
				  </table>
				  
				   <table border="0" id="table2" cellspacing="5" cellpadding="0" width="100%">					
					<tr>
						<td width="125">&nbsp;</td>
						<td width="200"> <c:if test="${param.success == 1}"> (保存成功) </c:if> 
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
</c:if>	

<c:if test="${!isOwner}">
	<b>拥有者（可以给其他人授权）：</b><br/>
	<pre>${membersForm.owners}</pre>
	
	<hr/>
	<b>贡献者：</b><br />
	<pre>${membersForm.commiters}</pre>
</c:if>			  	
</body>
</html>