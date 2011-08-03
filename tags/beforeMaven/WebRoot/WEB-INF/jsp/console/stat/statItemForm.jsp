<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/include/tags.jsp"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>设置统计项</title>
</head>

<body>
    <table width="96%" border="0" cellspacing="0" cellpadding="0" align="center">
      <tr>
        <td>&nbsp;</td>
      </tr>
      <!-- 输出列表 -->
              <tr><td>
			  	<form id="statItemForm" name="statItemForm" method="POST">
			  		<input type="hidden" name="id" value="<c:out value="${param.id}" default="-1" />" />
			  		<input type="hidden" name="groupId" value="<c:out value="${param.groupId}" default="-1" />" />
				  <table border="0" id="table2" cellspacing="5" cellpadding="0" width="100%">
					<tr>
						<td>&nbsp;</td>
						<td><b>名称：</b></td>
						<td align="left">
							<spring:bind path="statItemForm.statItem.name">
								<input type="text" class="input_title" id="<c:out value="${status.expression}" />" name="<c:out value="${status.expression}" />" value="<c:out value="${status.value}" />" />
								<%@ include file="/WEB-INF/jsp/include/error_inc.jsp" %>
							</spring:bind>
						</td>
						<td width="20">&nbsp;</td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td><b>描述：</b></td>
						<td align="left">
							<spring:bind path="statItemForm.statItem.description">
								<input type="text" class="input_title" id="<c:out value="${status.expression}" />" name="<c:out value="${status.expression}" />" value="<c:out value="${status.value}" />" />
								<%@ include file="/WEB-INF/jsp/include/error_inc.jsp" %>
							</spring:bind>
						</td>
						<td width="20">&nbsp;</td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td><b>quartz cron表达式：</b></td>
						<td align="left">
							<spring:bind path="statItemForm.statItem.cronExpression">
								<input <c:if test="${!statItemForm.newStatItem}">readonly</c:if> type="text" class="input_title" name="<c:out value="${status.expression}" />" id="<c:out value="${status.expression}" />" value="<c:out value="${status.value}" />" />
								分钟（0~59），小时（0~23），天（月）（0~31，但是你需要考虑你月的天数），月（0~11），天（星期）（1~7 1=SUN 或 SUN，MON，TUE，WED，THU，FRI，SAT），年份（1970－2099）
								<%@ include file="/WEB-INF/jsp/include/error_inc.jsp" %>
							</spring:bind>
						</td>
						<td width="20">&nbsp;</td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td><b>最大读取记录数：</b></td>
						<td align="left">
							<spring:bind path="statItemForm.statItem.fetchSize">
								<input type="text" class="input_title" name="<c:out value="${status.expression}" />" id="<c:out value="${status.expression}" />" value="<c:out value="${status.value}" />" />
								<%@ include file="/WEB-INF/jsp/include/error_inc.jsp" %>
							</spring:bind>
						</td>
						<td width="20">&nbsp;</td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td><b>最大发布记录数：</b></td>
						<td align="left">
							<spring:bind path="statItemForm.statItem.publishSize">
								<input type="text" class="input_title" name="<c:out value="${status.expression}" />" id="<c:out value="${status.expression}" />" value="<c:out value="${status.value}" />" />
								<%@ include file="/WEB-INF/jsp/include/error_inc.jsp" %>
							</spring:bind>
						</td>
						<td width="20">&nbsp;</td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td><b>数据提供者URL：</b></td>
						<td align="left">
							<spring:bind path="statItemForm.statItem.dataProviderUrl">
								<input type="text" class="input_title" name="<c:out value="${status.expression}" />" id="<c:out value="${status.expression}" />" value="<c:out value="${status.value}" />" />
								从此地址读取排行用原始json数据
								<%@ include file="/WEB-INF/jsp/include/error_inc.jsp" %>
							</spring:bind>
						</td>
						<td width="20">&nbsp;</td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td><b>数据提供者网页编码：</b></td>
						<td align="left">
							<spring:bind path="statItemForm.statItem.encoding">
								<input type="text" class="input_title" name="<c:out value="${status.expression}" />" id="<c:out value="${status.expression}" />" value="<c:out value="${status.value}" />" />
								<%@ include file="/WEB-INF/jsp/include/error_inc.jsp" %>
							</spring:bind>
						</td>
						<td width="20">&nbsp;</td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td><b>数据POST发布到URL：</b></td>
						<td align="left">
							<spring:bind path="statItemForm.statItem.dataPublisherUrl">
								<input type="text" class="input_title" name="<c:out value="${status.expression}" />" id="<c:out value="${status.expression}" />" value="<c:out value="${status.value}" />" />
								发布时，将排行榜数据POST到此URL中
								<%@ include file="/WEB-INF/jsp/include/error_inc.jsp" %>
							</spring:bind>
						</td>
						<td width="20">&nbsp;</td>
					</tr>
					
					<tr>
						<td>&nbsp;</td>
						<td><b>最短执行时间间隔：</b></td>
						<td align="left">
							<spring:bind path="statItemForm.statItem.timePointPrecision">
								<input type="text" class="input_title" name="<c:out value="${status.expression}" />" id="<c:out value="${status.expression}" />" value="<c:out value="${status.value}" />" />
								<%@ include file="/WEB-INF/jsp/include/error_inc.jsp" %>
							</spring:bind> 单位：分钟
						</td>
						<td width="20">&nbsp;</td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td><b>自动发布：</b></td>
						<td align="left">
							<spring:bind path="statItemForm.statItem.autoPublish">
								<input type="text" class="input_title" name="<c:out value="${status.expression}" />" id="<c:out value="${status.expression}" />" value="<c:out value="${status.value}" />" />
								自动将读取到的json数据根据模板合成发布格式，自动发布
								<%@ include file="/WEB-INF/jsp/include/error_inc.jsp" %>
							</spring:bind>
						</td>
						<td width="20">&nbsp;</td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td><b>记录排行中被隐藏的记录：</b></td>
						<td align="left">
							<spring:bind path="statItemForm.statItem.recordCheating">
								<input type="text" class="input_title" name="<c:out value="${status.expression}" />" id="<c:out value="${status.expression}" />" value="<c:out value="${status.value}" />" />
								<%@ include file="/WEB-INF/jsp/include/error_inc.jsp" %>
							</spring:bind>
						</td>
						<td width="20">&nbsp;</td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td><b>authKey：</b></td>
						<td align="left">
							<spring:bind path="statItemForm.statItem.authKey">
								<input type="text" class="input_title" name="<c:out value="${status.expression}" />" id="<c:out value="${status.expression}" />" value="<c:out value="${status.value}" />" />
								<%@ include file="/WEB-INF/jsp/include/error_inc.jsp" %>
							</spring:bind>&nbsp;
						</td>
						<td width="20">&nbsp;</td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td><b>统计最近多长时间的记录：</b></td>
						<td align="left">
							<spring:bind path="statItemForm.statItem.statBeforeMinutes">
								<input type="text" class="input_title" name="<c:out value="${status.expression}" />" id="<c:out value="${status.expression}" />" value="<c:out value="${status.value}" />" />
								<%@ include file="/WEB-INF/jsp/include/error_inc.jsp" %>
							</spring:bind>&nbsp;单位：分钟
						</td>
						<td width="20">&nbsp;</td>
					</tr>					
					<tr>
						<td>&nbsp;</td>
						<td><b>programId：</b></td>
						<td align="left">
							<spring:bind path="statItemForm.statItem.programId">
								<input type="text" class="input_title" name="<c:out value="${status.expression}" />" id="<c:out value="${status.expression}" />" value="<c:out value="${status.value}" />" />
								<%@ include file="/WEB-INF/jsp/include/error_inc.jsp" %>
							</spring:bind>&nbsp;
						</td>
						<td width="20">&nbsp;</td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td><b>统计记录是否允许被修改：</b></td>
						<td align="left">
							<spring:bind path="statItemForm.statItem.recordEditable">
								<input type="text" class="input_title" name="<c:out value="${status.expression}" />" id="<c:out value="${status.expression}" />" value="<c:out value="${status.value}" />" />
								<%@ include file="/WEB-INF/jsp/include/error_inc.jsp" %>
							</spring:bind>&nbsp;
						</td>
						<td width="20">&nbsp;</td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td><b>Velocity发布模板：</b></td>
						<td align="left">
							<spring:bind path="statItemForm.statItem.templateContent">
								<textarea class="input_title" rows="20" cols="80" name="<c:out value="${status.expression}" />" id="<c:out value="${status.expression}" />"><c:out value="${status.value}" /></textarea>
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