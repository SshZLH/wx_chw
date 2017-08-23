<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>身份验证结果</title>
		<meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no" />
		<script type="text/javascript">
		function closeWindow(){
			setInterval(function(){
				WeixinJSBridge.invoke('closeWindow',{},function(res){
    				//alert(res.err_msg);
				});
			},20);
		}
	 </script>
	</head>
	<body>
		<h2>系统繁忙，请刷新一下！</h2>
		<br>
		<br>
		<center>
			<h1>
				<input type="button" onclick="closeWindow()" value="返回"
					style="width: 95%; background-color: #00aa05; border: none; color: #ffffff; height: 50px; font-size: 30px;" />
			</h1>
		</center>
	</body>
</html>
