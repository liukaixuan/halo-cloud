<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/include/tags.jsp"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>过滤词导入</title>
</head>

<body>
    <table width="96%" border="0" cellspacing="0" cellpadding="0" align="center">
      <tr>
        <td>&nbsp;</td>
      </tr>
      <!-- 输出列表 -->
              <tr><td>
			  	<form id="filterWordImportForm" name="filterWordImportForm" method="POST" encType="multipart/form-data">
				  <input type="hidden" name="groupId" value="<c:out value='${param.groupId}' />">
				  
				  <table border="0" id="table2" cellspacing="5" cellpadding="0" width="100%">
					<tr>
						<td width="26">&nbsp;</td>
						<td width="140"><b>选择过滤词文件：</b></td>
						<td align="left">
							<spring:bind path="filterWordImportForm.uploadFile">
								<input type="file" class="input_title" id="<c:out value="${status.expression}" />" name="<c:out value="${status.expression}" />" value="<c:out value="${status.value}" />" />
								<%@ include file="/WEB-INF/jsp/include/error_inc.jsp" %>
							</spring:bind> 每行一个关键词的文本文件，文件编码必须为UTF-8
						</td>
						<td width="20">&nbsp;</td>
					</tr>
					<tr>
						<td width="26">&nbsp;</td>
						<td width="140"><b>过滤等级：</b></td>
						<td align="left">
							<spring:bind path="filterWordImportForm.level">
								<select class="input_title" name="<c:out value="${status.expression}" />" id="<c:out value="${status.expression}" />">
									<c:forEach begin="1" end="10" step="1" var="m_level">
										<c:if test="${m_level == 5}">
											<option selected value="<c:out value='${m_level}' />"><c:out value='${m_level}' /></a>
										</c:if>
										<c:if test="${m_level != 5}">
											<option value="<c:out value='${m_level}' />"><c:out value='${m_level}' /></a>
										</c:if>
									</c:forEach>
								</select>
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