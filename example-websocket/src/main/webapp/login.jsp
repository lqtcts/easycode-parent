<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>
<!DOCTYPE html>  
<html>  
<head>
<title>聊吧</title>
<style type="text/css">
     .container
     {
         font-family: "Courier New";
         width: 100%;
         height: 400px;
         overflow: auto;
         border: 1px solid black;
         background-color: #f0f0f0;
     }
</style>
</head>  
<body>
	<div id="skm_LockPane" class="LockOff"></div>
    <form id="form1">
        <!-- <h1>聊吧</h1> -->
        <br />
		<!-- 服务器地址: <input type="text" id="Connection" />  -->
		姓名： <input type="text" id="txtName" value=""/>
        <button id='ToggleConnection' type="button" onclick="creatCon();">连接</button>
        <br />
        <br />
        <div id='LogContainer' class='container'></div>
        <br />
        <div id='SendDataContainer'>
        	<table style="width: 100%">
        		<tr style="width: 100%">
        			<td>
        				<textarea id="DataToSend" rows="2" style="width: 100%;float: left;height: 75px"></textarea>
        			</td>
        			<td style="width: 60px">
        				<button id='SendData' type="button" onclick="SendDataClicked();" style="float: right; width: 100px;height: 81px; ">发送</button>
        			</td>
        		</tr>
        	</table>
        </div>
        <br />
    </form>
  <script type="text/javascript">
  	var iscon = false;  
  	var webSocket;
  	function creatCon(){
  		if(!document.getElementById("txtName").value){
  			alert("姓名不能为空");
  			return;
  		}
  		if(iscon){
  			iscon = false;
  			document.getElementById("ToggleConnection").innerHTML = "连接"; 
  			document.getElementById("txtName").disabled = false;
        	webSocket.close();
        	return;
  		}
	  	webSocket =  
	    	  new WebSocket("ws://192.168.1.144:8080/WebSocket/WebSocketController");  
	   	
	    webSocket.onerror = function(event) {  
	      	onError(event);
	    };  
	   	
	    webSocket.onopen = function(event) {  
	      	onOpen(event);
	    };  
	   	
	    webSocket.onmessage = function(event) {  
	      	onMessage(event);  
	    };  
  	}
  	
    function onMessage(event) {  
        addlog(event.data);
    }  
	
    function onOpen(event) {  
    	document.getElementById("ToggleConnection").innerHTML = "断开";
        document.getElementById("txtName").disabled = true;
        iscon = true;  
    }  
   
    function onError() {
    	document.getElementById("ToggleConnection").innerHTML = "连接"; 
    	iscon = false; 
    }  
    
    function SendDataClicked() {
    	if (document.getElementById("DataToSend").value.trim() != "") {  
      		webSocket.send("【"+document.getElementById("txtName").value+"】"+document.getElementById("DataToSend").value);  
      		addlog("【"+document.getElementById("txtName").value+"】"+document.getElementById("DataToSend").value);
      	 	document.getElementById("DataToSend").value = "";
    	}  
    }
    function addlog(Text) {
        document.getElementById("LogContainer").innerHTML = document.getElementById("LogContainer").innerHTML + Text + "<br />";
        var LogContainer = document.getElementById("LogContainer");
        LogContainer.scrollTop = LogContainer.scrollHeight;
    };
  </script>  
</body>  
</html> 