<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="org.guzz.web.context.GuzzWebApplicationContextUtil"%>
<%@page import="com.guzzservices.dir.*"%>
<%

String email = request.getParameter("email") ;
String password = request.getParameter("password") ;

ContactService msnContactService = (ContactService) GuzzWebApplicationContextUtil.getGuzzContext(session.getServletContext()).getService("msnContactService") ;

if(email != null){
	java.util.List<Contact> contacts = msnContactService.queryMsnContacts(email, password) ;

	for(Contact c : contacts){
		out.println(c.getName() + "&lt" + c.getEmail() + "&gt" + "<br/>") ;
	}
}

out.println("<hr/>") ;

%>

MSN帐号：<p/>
<form method="POST">
	Email: <input type="text" name="email" value="" /><br/>
	Password: <input type="password" name="password" value="" />
		
	<p/>
	<input type="submit" value="提交" />
</form>



