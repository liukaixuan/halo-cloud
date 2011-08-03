<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="org.guzz.web.context.GuzzWebApplicationContextUtil"%>
<%@page import="com.guzzservices.secure.WordFilterService"%>
<%@page import="com.guzzservices.secure.wordFilter.MatchResult"%>
<%

String groupId = request.getParameter("groupId") ;
String content = request.getParameter("content") ;

WordFilterService wordFilterService = (WordFilterService) GuzzWebApplicationContextUtil.getGuzzContext(session.getServletContext()).getService("wordFilterService") ;

if(content != null){
	MatchResult result = (MatchResult) wordFilterService.filterText(content, new String[]{groupId}, true) ;

	if(result != null){
		out.println("<p>命中词汇最高级别:" + result.getHighestLevel() + "</p>") ;
		out.println("<p>命中的词汇:" + result.getHittedContentList() + "</p>") ;
		out.println("<p>标记后内容:" + result.getMarkedContent() + "</p>") ;
	}else{
		out.println("<p>通过!" + "</p>") ;
	}
}

out.println("<hr/>") ;
%>

输入要测试的内容：<p/>
<form method="POST">
	<input type="hidden" name="groupId" value="<%=groupId%>" />
	
	<textarea name="content" rows="30" cols="80"></textarea>
	
	<p/>
	<input type="submit" value="提交" />

</form>

