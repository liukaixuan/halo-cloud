<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/include/tags.jsp"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>导出配置项</title>
</head>

<body>
    <table width="98%" border="0" cellspacing="0" cellpadding="0" align="center">
      <tr>
        <td>&nbsp;</td>
      </tr>
      <!-- 输出列表 -->
              <tr><td>			  		
				  <table border="0" id="table2" cellspacing="5" cellpadding="0" width="100%">
				    <tr>
						<td width="26">&nbsp;</td>
						<td><b>导出的配置项（复制以下代码，粘贴到导入框中进行导入）：</b>&nbsp;&nbsp;<a href="./configList.do?groupId=${groupId}">返回</a></td>
					</tr>
					<tr>
						<td width="26">&nbsp;</td>
						<td align="left">
							<textarea readonly rows="60" cols="120" name="json">${json}</textarea>
						</td>
					</tr>
				  </table>
				</td></tr>
              <!-- /输出列表 -->              
              <tr>
                <td height="5"></td>
              </tr>
</table>

</body>
</html>