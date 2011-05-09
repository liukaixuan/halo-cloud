<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/include/tags.jsp"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>导入配置项</title>
</head>

<body>
    <table width="98%" border="0" cellspacing="0" cellpadding="0" align="center">
      <tr>
        <td>&nbsp;</td>
      </tr>
      <!-- 输出列表 -->
              <tr><td>
			  	<form id="configImportForm" name="configImportForm" method="POST">
			  		<input type="hidden" name="groupId" value="${groupId}">
			  		
				  <table border="0" id="table2" cellspacing="5" cellpadding="0" width="100%">
				    <tr>
						<td width="26">&nbsp;</td>
						<td><b>导入配置项（粘贴从 导出配置项 复制的代码，并提交）：</b></td>
					</tr>
					<tr>
						<td width="26">&nbsp;</td>
						<td align="left">
							<textarea rows="30" cols="120" name="json"></textarea>
						</td>
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