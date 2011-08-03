<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.guzz.web.context.GuzzWebApplicationContextUtil"%>
<%@page import="org.guzz.util.RequestUtil"%>
<%@page import="com.guzzservices.dir.IPLocateService"%>
<%@page import="com.guzzservices.dir.ip.LocateResult"%>
<%

int count = RequestUtil.getParameterAsInt(request, "loop", 1) ;

IPLocateService s = (IPLocateService) GuzzWebApplicationContextUtil.getGuzzContext(session.getServletContext()).getService("chinaIPLocateService") ;

long begin = System.currentTimeMillis() ;

for(int i = 0 ; i < count ; i++){
	LocateResult result = s.findLocation("59.66.106.0") ;
	
	if(count == 1){
		if(result != null){
			out.println("<br>city:" + result.getCityName()) ;
			out.println("<br>detail:" + result.getDetailLocation()) ;
			out.println("<br>cityMarker:" + result.getCityMarker()) ;
		}else{
			out.println("<br>IP not found.") ;
		}
	}
}

long end = System.currentTimeMillis() ;

out.println("<p/>timeUsed:" + (end - begin) + "ms, loop:" + count) ;

LocateResult result = s.findLocation("59.66.106.0") ;

if(result != null){
	out.println("<p/>qinghua is:" + result.fullLocation()) ;
}

%>
