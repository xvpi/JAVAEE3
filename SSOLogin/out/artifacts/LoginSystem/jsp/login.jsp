<%@ page import="java.net.URL" %>
<%@ page import="java.net.HttpURLConnection" %>
<%@ page import="java.io.OutputStream" %>
<%@ page import="java.io.BufferedReader" %>
<%@ page import="java.io.InputStreamReader" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>登录页面</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
            background-image: url("../image/ssobgimg.jpg");
            background-size: cover;
        }

        .login-container {
            width: 300px;
            margin: 0 auto;
            padding: 20px;
            background-color: #fff;
            box-shadow: 0px 0px 10px 0px rgba(0, 0, 0, 0.1);
            margin-top: 100px;
        }

        .login-container h2 {
            text-align: center;
            color: #5C6670;
        }

        .login-container label {
            color: #5C6670;
        }

        .login-container input[type="text"],
        .login-container input[type="password"] {
            width: 90%;
            padding: 10px;
            margin: 5px 0 15px 0;
            border: 1px solid #ddd;
        }

        .login-container input[type="submit"] {
            width: 100%;
            padding: 10px;
            background-color: #007BFF;
            color: #fff;
            border: none;
        }
    </style>
</head>
<body>
<div class="login-container">
    <h2>用户登录</h2>
    <%
        String logout = request.getParameter("logout");
        String web = request.getParameter("web");
        if (logout != null && logout.equals("true")) {
            response.sendRedirect("http://www.LoginSystem.com:8081/clearcookie");
        } else {

            String token = null;
            Cookie[] cookies = ((HttpServletRequest) request).getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("token".equals(cookie.getName())) {
                        token = cookie.getValue();
                    }
                }
            }
            if (token != null && !token.isEmpty()) {
                if (web == null)
                    response.sendRedirect("http://www.LoginSystem.com:8081/jsp/main.jsp");
                else if (web.equals("web1"))
                    response.sendRedirect("http://www.CQU.com:8080?token=" + token);
                else if (web.equals("web2"))
                    response.sendRedirect("http://www.PKU.com:8082/?token=" + token);
                else {
                    response.sendRedirect("http://www.LoginSystem.com:8081/jsp/main.jsp");
                }
            }
        }
    %>
    <h2>授权登录到<%=web%></h2>
    <form action="/login?web=<%=web%>" method="post">
        <label for="username">用户名:</label><br>
        <input type="text" id="username" name="username"><br>
        <label for="password">密码:</label><br>
        <input type="password" id="password" name="password"><br>
        <input type="submit" value="登录">
    </form>
    <%
        String mess = (String) session.getAttribute("message"); // 接收后台传来的message
        if (mess != null && !mess.equals("")) { // 判断message
    %>
    <script type="text/javascript">
        alert("<%=mess%>"); // 弹出警示框
    </script>
    <%
            session.setAttribute("message", ""); // 将message值设为空，否则将一直弹出。
        }
    %>
</div>
</body>
</html>
