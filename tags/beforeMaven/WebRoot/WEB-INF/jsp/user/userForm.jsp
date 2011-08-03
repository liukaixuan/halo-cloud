<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/include/tags.jsp"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>注册用户</title>
	</head>

	<body>
		<table width="100%" border="0" cellspacing="0" cellpadding="0" align="center">
			<tr>
				<td>
					<form id="userModel" name="userModel" method="POST">
							<h2>
								注册新用户
							</h2>
							<table border="0" id="table2" cellspacing="5" cellpadding="0"
								width="100%">
								<tr>
									<td width="26">
										&nbsp;
									</td>
									<td width="160">
										<b>登录邮箱：</b>
									</td>
									<td align="left">
										<spring:bind path="user.email">
											<input type="text" class="input_title" maxlength="64"
												name="<c:out value="${status.expression}"  />" value="<c:out value="${status.value}" />"/>
											<%@ include file="/WEB-INF/jsp/include/error_inc.jsp"%> 以后不能修改。请真实填写，以在服务升级时提前获得通知。
										</spring:bind>
									</td>
									<td width="20">
										&nbsp;
									</td>
								</tr>
								<tr>
									<td width="26">
										&nbsp;
									</td>
									<td width="160">
										<b>密码：</b>
									</td>
									<td align="left">
										<spring:bind path="user.password">
											<input type="password" class="input_title" maxlength="64"
												id="<c:out value="${status.expression}" />"
												name="<c:out value="${status.expression}" />"
												value="<c:out value="${status.value}" />"/>&nbsp;密码以MD5加密后保存，放心使用。
											<%@ include file="/WEB-INF/jsp/include/error_inc.jsp"%>
										</spring:bind>
									</td>
									<td width="20">
										&nbsp;
									</td>
								</tr>
								<tr>
									<td width="26">
										&nbsp;
									</td>
									<td width="160">
										<b>显示昵称：</b>
									</td>
									<td align="left">
										<spring:bind path="user.nickName">
											<input type="text" class="input_title" maxlength="32"
												name="<c:out value="${status.expression}" />"
												id="<c:out value="${status.expression}" />"
												value="<c:out value="${status.value}" />" 
												/>
											<%@ include file="/WEB-INF/jsp/include/error_inc.jsp"%> 以后不能修改.
										</spring:bind>
									</td>
									<td width="20">
										&nbsp;
									</td>
								</tr>
						</table>
					
						<table border="0" id="table2" cellspacing="5" cellpadding="0"
							width="100%">
							<tr>
								<td width="125">
									&nbsp;
								</td>
								<td width="200">
									<input type="submit" name="submit" id="submit" value="提交"/>
									<input type="reset" class="smb_btn" name="submit" value="取消" />
								</td>
								<td>
									&nbsp;
								</td>
								<td width="20">
									&nbsp;
								</td>
							</tr>
						</table>

					</form>
				</td>
			</tr>
			<!-- /输出列表 -->
			<tr>
				<td height="5"></td>
			</tr>

		</table>

	</body>
</html>
