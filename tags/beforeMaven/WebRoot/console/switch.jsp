<%@ page contentType="text/html;charset=UTF-8"%>
<html>
<head>
    <script>
		var currentChoice = true;
    	function changeDisplay(){
    		var sh = document.getElementById("switch");
    		    		
    		//隐藏
    		if(currentChoice){
    			sh.src = "../images/icons/open.gif";
    			sh.alt = "打开";
     			window.top.document.getElementById("indexFrame").cols = "0,7,*";
    		}
    		else{
    			sh.src = "../images/icons/close.gif";
    			sh.alt = "关闭";
     			window.top.document.getElementById("indexFrame").cols = "200,7,*";
    		}
    		currentChoice = !currentChoice;
    	}
    </script>
    <style>
    	body{
    		margin:0px;
    		padding:0px;
    		background:#c0c0c0 url(../images/icons/closopenbg.gif) middle top repeat-y;
    	}
	   	#disDiv{
	   		position:absolute;
	   		cursor:pointer;
	   		left:0px;
	   		top:40%;
	   		width:7px;
	   		height:53px;
	   	}
    </style>
</head>
<body>
<div id="disDiv"><img id="switch" alt="关闭" onclick="changeDisplay();" src="../images/icons/close.gif" width="7" height="53"></div>
</body>
</html>