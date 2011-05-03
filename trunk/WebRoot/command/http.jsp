<%@ page contentType="text/html;charset=UTF-8"%><%@ page session="false" %><%@page import="org.apache.commons.codec.digest.DigestUtils"%><%@page import="org.apache.commons.codec.binary.Base64"%><%@page import="com.guzzservices.rpc.server.CommandServerService"%><%@page import="com.guzzservices.rpc.server.CommandRequest"%><%@page import="com.guzzservices.rpc.server.CommandResponse"%><%@page import="org.guzz.web.context.GuzzWebApplicationContextUtil"%><%

CommandServerService commandServerService = (CommandServerService) application.getAttribute("commandServerService") ;
if(commandServerService == null){
	commandServerService = (CommandServerService) GuzzWebApplicationContextUtil.getGuzzContext(request.getSession().getServletContext()).getService("commandServerService") ;
	application.setAttribute("commandServerService", commandServerService) ;
}

CommandRequest cr = new CommandRequest() ;

cr.command = request.getParameter("command") ;
cr.isStringParam = "1".equalsIgnoreCase(request.getParameter("isStringParam")) || "true".equalsIgnoreCase(request.getParameter("isStringParam")) ;
String param = request.getParameter("param") ;

if(param != null){
	if(cr.isStringParam){
		cr.paramS = param ;
	}else if(param.length() == 0){
		cr.paramB = new byte[0] ;
	}else{
		cr.paramB = Base64.decodeBase64(param) ; 
	}
}

CommandResponse resp = commandServerService.executeCommand(cr) ;

response.setHeader("guzzCommandServiceException", resp.isException ? "1" : "0") ;
response.setHeader("guzzCommandServiceString", resp.isStringResult ? "1" : "0") ;

try{
	if(resp.isStringResult){
		if(resp.resultS == null){
			response.setHeader("guzzCommandServiceLength", "-1") ;
		}else{
			response.setHeader("guzzCommandServiceLength", "" + resp.resultS.length()) ;
			
			response.getWriter().write(resp.resultS) ;
		}
	}else{
		if(resp.resultB == null){
			response.setHeader("guzzCommandServiceLength", "-1") ;
		}else{
			response.setHeader("guzzCommandServiceLength", "" + resp.resultB.length) ;
			
			response.getWriter().write(Base64.encodeBase64String(resp.resultB)) ;
		}
	}
}catch(Exception e){
	e.printStackTrace() ;
	
}

%>