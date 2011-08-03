<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/include/tags.jsp"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>设置统计临时结果</title>
</head>

<body>
    <table width="96%" border="0" cellspacing="0" cellpadding="0" align="center">
      <tr>
        <td>&nbsp;</td>
      </tr>
      <!-- 输出列表 -->
              <tr><td>
			  	<form id="topRecordForm" name="topRecordForm" method="POST">
			  		<input type="hidden" name="id" value="<c:out value="${param.id}" default="-1" />" />
			  		<input type="hidden" name="statId" value="<c:out value="${param.statId}" default="-1" />" />
				  <table border="0" id="table2" cellspacing="5" cellpadding="0" width="100%">
					<tr>
						<td>&nbsp;</td>
						<td width="180"><b>主ID：</b></td>
						<td align="left">
							<spring:bind path="topRecordForm.topRecord.objectId">
								<input type="text" class="input_title" id="<c:out value="${status.expression}" />" name="<c:out value="${status.expression}" />" value="<c:out value="${status.value}" />" />
								<%@ include file="/WEB-INF/jsp/include/error_inc.jsp" %>
							</spring:bind>
						</td>
						<td width="20">&nbsp;</td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td><b>标题：</b></td>
						<td align="left">
							<spring:bind path="topRecordForm.topRecord.objectTitle">
								<input type="text" class="input_title" id="<c:out value="${status.expression}" />" name="<c:out value="${status.expression}" />" value="<c:out value="${status.value}" />" />
								<%@ include file="/WEB-INF/jsp/include/error_inc.jsp" %>
							</spring:bind>
						</td>
						<td width="20">&nbsp;</td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td><b>地址：</b></td>
						<td align="left">
							<spring:bind path="topRecordForm.topRecord.objectURL">
								<input type="text" class="input_title" name="<c:out value="${status.expression}" />" id="<c:out value="${status.expression}" />" value="<c:out value="${status.value}" />" />
								<%@ include file="/WEB-INF/jsp/include/error_inc.jsp" %>
							</spring:bind>
						</td>
						<td width="20">&nbsp;</td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td><b>操作次数：</b></td>
						<td align="left">
							<spring:bind path="topRecordForm.topRecord.opTimes">
								<input type="text" class="input_title" name="<c:out value="${status.expression}" />" id="<c:out value="${status.expression}" />" value="<c:out value="${status.value}" />" />
								<%@ include file="/WEB-INF/jsp/include/error_inc.jsp" %>
							</spring:bind>
						</td>
						<td width="20">&nbsp;</td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td><b>发布时排序位置：</b></td>
						<td align="left">
							<spring:bind path="topRecordForm.topRecord.objectOrder">
								<input type="text" class="input_title" name="<c:out value="${status.expression}" />" id="<c:out value="${status.expression}" />" value="<c:out value="${status.value}" />" />
								<%@ include file="/WEB-INF/jsp/include/error_inc.jsp" %>
							</spring:bind>
						</td>
						<td width="20">&nbsp;</td>
					</tr>			
					<tr>
						<td>&nbsp;</td>
						<td><b>创建时间：</b></td>
						<td align="left">
							<spring:bind path="topRecordForm.topRecord.objectCreatedTime">
								<input type="text" class="input_title" name="<c:out value="${status.expression}" />" id="<c:out value="${status.expression}" />" value="<c:out value="${status.value}" />" />
								<%@ include file="/WEB-INF/jsp/include/error_inc.jsp" %>
							</spring:bind>
						</td>
						<td width="20">&nbsp;</td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td><b>自定义1：</b></td>
						<td align="left">
							<spring:bind path="topRecordForm.topRecord.extra1">
								<input type="text" class="input_title" name="<c:out value="${status.expression}" />" id="<c:out value="${status.expression}" />" value="<c:out value="${status.value}" />" />
								<%@ include file="/WEB-INF/jsp/include/error_inc.jsp" %>
							</spring:bind>
						</td>
						<td width="20">&nbsp;</td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td><b>自定义2：</b></td>
						<td align="left">
							<spring:bind path="topRecordForm.topRecord.extra2">
								<input type="text" class="input_title" name="<c:out value="${status.expression}" />" id="<c:out value="${status.expression}" />" value="<c:out value="${status.value}" />" />
								<%@ include file="/WEB-INF/jsp/include/error_inc.jsp" %>
							</spring:bind>
						</td>
						<td width="20">&nbsp;</td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td><b>自定义3：</b></td>
						<td align="left">
							<spring:bind path="topRecordForm.topRecord.extra3">
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