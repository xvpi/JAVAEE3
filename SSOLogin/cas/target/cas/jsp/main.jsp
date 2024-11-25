<%@ page import="com.xvpi.cas.service.UserService" %>
<%@ page import="java.sql.SQLException" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="javax.servlet.http.Cookie" %>
<%@ page import="javax.servlet.http.HttpServletRequest" %>
<%@ page import="java.util.Objects" %>
<%@ page import="com.xvpi.cas.domain.User" %>
<%@ page import="java.net.URL" %>
<%@ page import="java.net.HttpURLConnection" %>
<%@ page import="java.io.OutputStream" %>
<%@ page import="java.io.BufferedReader" %>
<%@ page import="java.io.InputStreamReader" %>
<%@ page import="java.io.OptionalDataException" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Main Page</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
            background-image: url("../image/cqu_bg.png");
            background-size: cover;
        }

        h1 {
            color: #333;
        }

        button {

        }

        .welcome-container {
            width: 300px;
            margin: 0 auto;
            padding: 20px;
            background-color: #fff;
            box-shadow: 0px 0px 10px 0px rgba(0, 0, 0, 0.1);
            margin-top: 100px;
        }

        input[type=submit] {
            background-color: #4CAF50; /* Green */
            border: none;
            color: white;
            padding: 15px 32px;
            text-align: center;
            text-decoration: none;
            display: inline-block;
            font-size: 16px;
            margin: 4px 2px;
            cursor: pointer;
        }
        table {
            border-collapse: collapse;
            width: 100%;
        }
        th, td {
            border: 1px solid #dddddd;
            text-align: left;
            padding: 8px;
        }
        th {
            background-color: #f2f2f2;
        }
        tr:nth-child(even) {
            background-color: #f2f2f2;
        }
    </style>
</head>
<body>
<%
    Cookie[] cookies = ((HttpServletRequest) request).getCookies();
    String username = null;
    String token = null;
    if (cookies != null) {
        for (Cookie cookie : cookies) {
            if ("token".equals(cookie.getName())) {
                token = cookie.getValue();
                if (Objects.equals(token, "")) {
                    token = null;
                }
            }
            if ("username".equals(cookie.getName())) {
                username = cookie.getValue();
            }
        }
    }
    if (token != null) {
        // 验证token有效性
        try {
            if (UserService.getInstance().valToken(token) == null) {
                response.sendRedirect("http://www.Cas.com:8081/jsp/login.jsp");
            } else {

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    } else {
        response.sendRedirect("http://www.Cas.com:8081/jsp/login.jsp");
    }
%>


<div class="welcome-container">
    <h2>欢迎登录，<%=username%> 同学</h2>
    <form action="/jump?web=web1" method="post">
        <input type="submit" value="web1">
    </form>
    <form action="/jump?web=web2" method="post">
        <input type="submit" value="web2">
    </form>

    <form action="/logout?token=<%=token%>" method="post">
        <input type="submit" value="退出登录">
    </form>
    <!-- 新增查看历史记录的按钮 -->
    <form action="/history" method="get">
        <input type="submit" value="查看历史记录">
    </form>
    <form action="/user-management" method="get">
        <input type="submit" value="账号禁用">
    </form>

    <table border="1">
        <tr>
            <th>id</th>
            <th>用户名</th>
            <th>登录时间</th>
        </tr>
        <%
            String targetUrl = "http://www.Cas.com:8081/ShowAllUser";
            // 创建连接
            URL url = new URL(targetUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            // 设置请求方法为 POST
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            // 发送请求
            String[][] table;
            try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
                String response1 = br.readLine();
                System.out.println(response1);
                String[] records = response1.split(",");
                table = new String[records.length][3];
                for (int i = 0; i < records.length; ++i) {
                    table[i][0] = records[i].split("  ")[2].split(" = ")[1];
                    table[i][1] = records[i].split("  ")[4].split(" = ")[1];
                    table[i][2] = records[i].split("  ")[8].split(" = ")[1];
                }
            }
            connection.disconnect();
            for (int i = 0; i < table.length; i++) { %>
        <tr>
            <td><%= table[i][0] %>
            </td>
            <td><%= table[i][1] %>
            </td>
            <td><%= table[i][2] %>
            </td>
        </tr>
        <% } %>
    </table>
</div>
</body>
</html>

