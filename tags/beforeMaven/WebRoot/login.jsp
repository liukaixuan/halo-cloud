<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/include/tags.jsp"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
	<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>登录管理台</title>
</head>

<body onload="loginForm.userName.focus();">
	<form name="loginForm" method="post" action="./login.do" target="_top">
		<table border="0" width="100%" align="center">
			  <tr height="50px;">
                <td align="right">&nbsp;</td>
                <td width="10px;">&nbsp;</td>
                <td align="left">登录服务控制台</td>
              </tr>
              <tr>
                <td>&nbsp;</td>
                <td width="10px;">&nbsp;</td>
                <td><font color="red">${param.msg}</font></td>
              </tr>
              <tr>
                <td align="right" width="150px;">登录名：</td>
                <td>&nbsp;</td>
                <td><input type="text" style='width:200px;height:20px' id="login_userName" name="userName" size="15" ></td>
              </tr>
              <tr>
                <td align="right">密码：</td>
                <td>&nbsp;</td>
                <td><input type="password" style='width:200px;height:20px' id="login_password" name="password" size="15" maxlength="64" ></td>
              </tr>
              <tr>
                <td align="right">&nbsp;<a href="userReg.do">开通帐号</a></td>
                <td>&nbsp;</td>
                <td><input type="submit" name="Submit" value="登录" ></td>
              </tr>
		</table>
	</form>
</body>

</html>
