<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/include/tags.jsp"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>设置系统配置</title>
</head>

<body>
    <table width="98%" border="0" cellspacing="0" cellpadding="0" align="center">
      <tr>
        <td>&nbsp;</td>
      </tr>
      <!-- 输出列表 -->
              <tr><td>
			  	<form id="configForm" name="configForm" method="POST">
			  		<input type="hidden" name="id" value="<c:out value="${param.id}" default="" />" />
			  		<input type="hidden" name="groupId" value="<c:out value="${param.groupId}" default="" />" />
			  		
				  <table border="0" id="table2" cellspacing="5" cellpadding="0" width="100%">
				    <tr>
						<td width="26">&nbsp;</td>
						<td><b>显示名称：</b></td>
						<td align="left">
							<spring:bind path="configForm.configuration.name">
								<input type="text" size="70" class="input_title" id="<c:out value="${status.expression}" />" name="<c:out value="${status.expression}" />" value="<c:out value="${status.value}" />" />
								<%@ include file="/WEB-INF/jsp/include/error_inc.jsp" %>
							</spring:bind>
						</td>
						<td width="20">&nbsp;</td>
					</tr>
					<tr>
						<td width="26">&nbsp;</td>
						<td><b>程序用参数名：</b></td>
						<td align="left">
							<spring:bind path="configForm.configuration.parameter">
								<input type="text" size="70" class="input_title" id="<c:out value="${status.expression}" />" name="<c:out value="${status.expression}" />" value="<c:out value="${status.value}" />" />
								<%@ include file="/WEB-INF/jsp/include/error_inc.jsp" %>
							</spring:bind>
						</td>
						<td width="20">&nbsp;</td>
					</tr>
					<tr>
						<td width="26">&nbsp;</td>
						<td><b>参数值：</b></td>
						<td align="left">
							<spring:bind path="configForm.configuration.value">								
								<c:if test="${'text' eq configForm.configuration.type }">
									<textarea rows="20" cols="70" name="<c:out value="${status.expression}" />" ><c:out value="${status.value}" /></textarea>
								</c:if>
								
								<c:if test="${'text' ne configForm.configuration.type }">
									<input type="text"" size="70" class="input_title" name="<c:out value="${status.expression}" />" id="<c:out value="${status.expression}" />" value="<c:out value="${status.value}" />" />
								</c:if>
								
								<%@ include file="/WEB-INF/jsp/include/error_inc.jsp" %>
							</spring:bind>
						</td>
						<td width="20">&nbsp;</td>
					</tr>
					<tr>
						<td width="26">&nbsp;</td>
						<td><b>数据类型：</b></td>
						<td align="left">
							<spring:bind path="configForm.configuration.type">
								<select class="input_title" name="<c:out value="${status.expression}" />" id="<c:out value="${status.expression}" />">
									<c:forEach items="${dataTypes}" var="m_type">
										<option value="${m_type}" <c:if test="${m_type eq status.value}">selected</c:if>>${m_type}</option>
									</c:forEach>
								</select>
								<%@ include file="/WEB-INF/jsp/include/error_inc.jsp" %>
							</spring:bind>&nbsp;
						</td>
						<td width="20">&nbsp;</td>
					</tr>
					<tr>
						<td width="26">&nbsp;</td>
						<td><b>有效值：</b></td>
						<td align="left">
							<spring:bind path="configForm.configuration.validValues">
								<input type="text"" size="50" class="input_title" name="<c:out value="${status.expression}" />" id="<c:out value="${status.expression}" />" value="<c:out value="${status.value}" />" />
								&nbsp;用英文分号分割
								<%@ include file="/WEB-INF/jsp/include/error_inc.jsp" %>
							</spring:bind>
						</td>
						<td width="20">&nbsp;</td>
					</tr>
					<tr>
						<td width="26">&nbsp;</td>
						<td><b>帮助：</b></td>
						<td align="left">
							<spring:bind path="configForm.configuration.description">
								<textarea rows="20" cols="70" name="<c:out value="${status.expression}" />" ><c:out value="${status.value}" /></textarea>
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