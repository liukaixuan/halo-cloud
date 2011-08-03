<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/include/tags.jsp"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>过滤词导入</title>
</head>

<body>

    导入失败的过滤词：<c:out value="${failed}" />
    
 <hr>
    导入成功的过滤词：<c:out value="${successed}" />
 <hr>
  <a href="filterWordList.do?groupId=${groupId}">返回</a>
  
</body>
</html>