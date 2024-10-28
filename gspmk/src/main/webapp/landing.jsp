<%-- 
    Document   : landing
    Created on : Oct 6, 2024, 2:08:06 PM
    Author     : vdang
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>GSPMK</title>
		<link rel="icon" href="${pageContext.request.contextPath}/static/images/icon.jpg" type="image/x-icon"> 
    </head>
    <body>
        <h1>landing page</h1>
		<button onclick="log_in_button()">log in</button>
    </body>
	<script>
		function log_in_button(){
			window.location.href = 'login';
		}
		
	</script>
</html>
