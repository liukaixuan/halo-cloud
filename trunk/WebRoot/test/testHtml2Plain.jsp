<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.guzz.web.context.GuzzWebApplicationContextUtil"%>
<%@page import="org.guzz.util.RequestUtil"%>
<%@page import="com.guzzservices.text.Html2PlainExtractService"%>
<%@page import="com.guzzservices.text.PlainExtractResult"%>
<%

Html2PlainExtractService html2PlainExtractService = (Html2PlainExtractService) GuzzWebApplicationContextUtil.getGuzzContext(session.getServletContext()).getService("html2PlainExtractService") ;

String content = request.getParameter("content") ;

PlainExtractResult result =  html2PlainExtractService.extractTextWithAllImages(content, 0) ;

out.println("plainContent:" + result.getPlainText() + "<hr/>") ;
out.println("images:" + (result.getImages() == null ? null : Arrays.asList(result.getImages())) + "<hr/>") ;
out.println("imageTitles:" + (result.getImageTitles() == null ? null : Arrays.asList(result.getImageTitles())) + "<hr/>") ;

%>


<form method="POST">
	<textarea name="content" rows="30" cols="80"></textarea>
	
	<p/>
	<input type="submit" />

</form>