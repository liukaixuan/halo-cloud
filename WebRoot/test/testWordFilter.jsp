<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.guzz.web.context.GuzzWebApplicationContextUtil"%>
<%@page import="org.guzz.util.RequestUtil"%>
<%@page import="com.guzzservices.secure.WordFilterService"%>
<%@page import="com.guzzservices.secure.wordFilter.MatchResult"%>
<%

int count = RequestUtil.getParameterAsInt(request, "loop", 1) ;

String content = "平反三哥" ;

/*
for(int i = 0 ; i < 10 ; i++){
	content = content + content ;
}
*/

WordFilterService wordFilterService = (WordFilterService) GuzzWebApplicationContextUtil.getGuzzContext(session.getServletContext()).getService("wordFilterService") ;

long begin = System.currentTimeMillis() ;

for(int i = 0 ; i < count ; i++){
	MatchResult result = (MatchResult) wordFilterService.filterText(content, new String[]{null, "b3vh5xmun0r2z4pkil2g5rpxnt2mu76n0r7qqoa"}, true) ;

	if(count == 1){
		if(result != null){
			out.println("<br>getHighestLevel:" + result.getHighestLevel()) ;
			out.println("<br>getHittedContentList:" + result.getHittedContentList()) ;
			out.println("<br>getMarkedContent:" + result.getMarkedContent()) ;
			out.println("<br>getMatchedContentList:" + result.getMatchedContentList(",", 5)) ;
		}else{
			out.println("<br>passed!") ;
		}
	}
}

long end = System.currentTimeMillis() ;

out.println("<p/>timeUsed:" + (end - begin) + "ms, loop:" + count) ;

%>
