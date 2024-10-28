<%-- 
    Document   : login
    Created on : Oct 6, 2024, 2:29:37 PM
    Author     : vdang
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Trang Đăng Nhập</title>
        <link rel="icon" href="${pageContext.request.contextPath}/static/images/icon.jpg" type="image/x-icon">
        <link rel="stylesheet" href="static/css/login.css?v=1.0">
    </head>
    <body>
        <!-- Container chính -->
        <div class="container">
            <!-- Nút hiển thị chức năng đăng nhập và đăng ký -->
			<h2>GSPMK</h2>
            <div class="button-container">
				<button id="login-btn" class="toggle-btn">Login</button>
				<button id="signup-btn" class="toggle-btn">Sign Up</button>
			</div>

            <!-- Form đăng nhập -->
            <div id="login-section" class="form-section">
                <h1>Login</h1>
                <hr>
                <form action="validate/CheckValidLogin" method="POST">
                    <fieldset>
                        <legend></legend>
                        <label for="username">Tên người dùng:</label>
                        <input type="text" id="username" name="username" required>
                        <span id="checking-state-login"></span>
                        <br>
                        <label for="password">Mật khẩu:</label>
                        <input type="password" id="password" name="password" required>
                        <c:if test="${not empty param.error}">
                            <span class="error-msg">Wrong password</span>
                        </c:if>
                    </fieldset>
                    <button type="submit" class="submit-btn" onclick="check_login(event, this.form)">Đăng nhập</button>
                </form>
            </div>

            <!-- Form đăng ký -->
            <div id="signup-section" class="form-section">
                <h1>Sign Up</h1>
                <hr>
                <form action="validate/CheckValidAndSignup" method="POST">
                    <fieldset>
                        <legend></legend>
                        <label for="username-signup">Tên người dùng:</label>
                        <input type="text" id="username-signup" name="username-signup" required>
                        <span id="checking-state-signup"></span>
                        <br>
                        <label for="password-signup">Mật khẩu:</label>
                        <input type="password" id="password-signup" name="password-signup" required>
                        <br>
                        <label for="repeatpassword-signup">Nhắc lại mật khẩu:</label>
                        <input type="password" id="repeatpassword-signup" name="repeatpassword-signup" required>
                        <span id="password-not-match" class="error-msg"></span>
                    </fieldset>
                    <button type="submit" class="submit-btn" onclick="check_signup(event, this.form)">Đăng ký</button>
                </form>
            </div>
        </div>

        <!-- JavaScript -->
        <script src="static/js/login.js?v=1.0"></script>
        <script src="static/js/loginstyle.js?v=1.0"></script>
    </body>
</html>

