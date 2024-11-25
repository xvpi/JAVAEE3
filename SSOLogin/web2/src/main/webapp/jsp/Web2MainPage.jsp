<%@ page import="java.util.Objects" %>
<%@ page import="java.util.Random" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page import="java.net.URL" %>
<%@ page import="java.net.HttpURLConnection" %>
<%@ page import="java.io.BufferedReader" %>
<%@ page import="java.io.InputStreamReader" %>
<%@ page import="java.io.OutputStream" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    String token = null;
    Cookie[] cookies = ((HttpServletRequest) request).getCookies();
    if (cookies != null) {
        for (Cookie cookie : cookies) {
            if ("token".equals(cookie.getName())) {
                token = cookie.getValue();
            }
        }
    }
    if (token == null || token.equals("")) {
        response.sendRedirect("http://www.Cas.com:8081/jsp/login.jsp?web=web2");
    } else {
        String targetUrl = "http://www.Cas.com:8081/ValToken";
        // 创建连接
        URL url = new URL(targetUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        // 设置请求方法为 POST
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

        // 构建请求参数
        String postData = "token=" + token;

        // 发送请求
        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = postData.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        // 获取响应
        try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
            String response1 = br.readLine();
            String valSuccess = response1.split(";")[0];
            String username = response1.split(";")[1];
            if (valSuccess.equals("true")) {
                // 验证token通过，存到cookie
                Cookie cookieUsername = new Cookie("username", username);
                response.addCookie(cookieUsername);
                Cookie cookieToken = new Cookie("token", token);
                response.addCookie(cookieToken);
            } else {
                response.sendRedirect("http://www.Cas.com:8081/jsp/login.jsp?web=web2");
            }
        } catch (Exception e) {
            response.sendRedirect("http://www.Cas.com:8081/jsp/login.jsp?web=web2");
        }
        connection.disconnect();
    }

    // 处理生成随机数
    String randomString = "";
    String length = request.getParameter("length");
    String includeNumbers = request.getParameter("includeNumbers");
    String includeLowercase = request.getParameter("includeLowercase");
    String includeUppercase = request.getParameter("includeUppercase");
    String includeSymbols = request.getParameter("includeSymbols");

    if (length != null) {
        int len = Integer.parseInt(length);
        List<String> charPool = new ArrayList<>();
        if ("on".equals(includeNumbers)) {
            for (char c = '0'; c <= '9'; c++) charPool.add(String.valueOf(c));
        }
        if ("on".equals(includeLowercase)) {
            for (char c = 'a'; c <= 'z'; c++) charPool.add(String.valueOf(c));
        }
        if ("on".equals(includeUppercase)) {
            for (char c = 'A'; c <= 'Z'; c++) charPool.add(String.valueOf(c));
        }
        if ("on".equals(includeSymbols)) {
            String symbols = "~!@#$%^&*()_+";
            for (int i = 0; i < symbols.length(); i++) {
                charPool.add(String.valueOf(symbols.charAt(i)));
            }
        }

        Random rand = new Random();
        for (int i = 0; i < len; i++) {
            randomString += charPool.get(rand.nextInt(charPool.size()));
        }
    }

%>

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
            background-image: url("../image/cqu_bg.png");
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

        .login-container input[type="text"],
        .login-container input[type="password"] {
            width: 90%;
            padding: 10px;
            margin: 5px 0 15px 0;
            border: 1px solid #ddd;
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

        /* Random string generation section */
        .random-string-container {
            margin-top: 30px;
            padding: 20px;
            background-color: #fff;
            box-shadow: 0px 0px 10px 0px rgba(0, 0, 0, 0.1);
            width: 80%;
            margin: 20px auto;
        }

        .random-string-container h3 {
            text-align: center;
            color: #5C6670;
        }
    </style>
</head>
<body>
<%
    String username = null;
    if (cookies != null) {
        for (Cookie cookie : cookies) {
            if ("username".equals(cookie.getName())) {
                username = cookie.getValue();
            }
        }
    }
%>

<div class="login-container">
    <h2>欢迎访问随机数生成工具箱</h2>
    <h2>你好:<%=username%> 同学</h2>
    <form action="http://www.Web1.com:8080?token=<%= token %>" method="post">
        <input type="submit" value="web1">
    </form>
    <form action="/logout?token=<%= token %>" method="post">
        <input type="submit" value="退出登录">
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
            String[][] table;
            try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
                String response1 = br.readLine();
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
<div class="random-string-container"><h3>随机字符串生成器</h3>
    <form method="post"><label for="length">生成长度:</label> <input type="number" id="length" name="length" min="1"
                                                                     max="20" required> <br> <label
            for="includeNumbers">包含数字:</label> <input type="checkbox" id="includeNumbers" name="includeNumbers">
        <br> <label for="includeLowercase">包含小写字母:</label> <input type="checkbox" id="includeLowercase"
                                                                        name="includeLowercase"> <br> <label
                for="includeUppercase">包含大写字母:</label> <input type="checkbox" id="includeUppercase"
                                                                    name="includeUppercase"> <br> <label
                for="includeSymbols">包含符号 (~!@#$%^&*()_+):</label> <input type="checkbox" id="includeSymbols"
                                                                              name="includeSymbols"> <br><br> <input
                type="submit" value="生成随机数"></form>
    <div><p><strong>生成的随机字符串:</strong></p>
        <p><%= randomString %>
        </p></div>
</div>
</body>
</html>
