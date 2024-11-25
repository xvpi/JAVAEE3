<%@ page import="java.util.Objects" %>
<%@ page import="java.net.URL" %>
<%@ page import="java.net.HttpURLConnection" %>
<%@ page import="java.io.BufferedReader" %>
<%@ page import="java.io.InputStreamReader" %>
<%@ page import="java.io.OutputStream" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    String token=null;
    Cookie[] cookies = ((HttpServletRequest) request).getCookies();
    if (cookies != null) {
        for (Cookie cookie : cookies) {
            if ("token".equals(cookie.getName())) {
                token = cookie.getValue();
            }
        }
    }
    if(token==null||token.equals("")){
        response.sendRedirect("http://www.Cas.com:8081/jsp/login.jsp?web=web1");
    }
    else {
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
        System.out.println(token);
        // 获取响应
        try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
            String response1 = br.readLine();
            System.out.println("AAAA"+response1);
            String valSuccess = response1.split(";")[0];
            String username = response1.split(";")[1];
            if (valSuccess.equals("true")) {
                // 验证token通过，存到cookie
                Cookie cookieUsername = new Cookie("username", username);
                response.addCookie(cookieUsername);
                Cookie cookieToken = new Cookie("token", token);
                response.addCookie(cookieToken);
            } else {
                response.sendRedirect("http://www.Cas.com:8081/jsp/login.jsp?web=web1");
            }
        }catch(Exception e){
            response.sendRedirect("http://www.Cas.com:8081/jsp/login.jsp?web=web1");
        }
        // 关闭连接
        connection.disconnect();
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
            box-shadow: 0px 0px 10px 0px rgba(0,0,0,0.1);
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

        /* Git commands section */
        .git-commands-container {
            margin-top: 30px;
            padding: 20px;
            background-color: #fff;
            box-shadow: 0px 0px 10px 0px rgba(0,0,0,0.1);
            width: 80%;
            margin: 20px auto;
        }
        .git-commands-container h3 {
            text-align: center;
            color: #5C6670;
        }
        .git-commands-table {
            width: 100%;
            border-collapse: collapse;
        }
        .git-commands-table th, .git-commands-table td {
            padding: 10px;
            border: 1px solid #ddd;
        }
        .git-commands-table th {
            background-color: #f2f2f2;
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
    <h2>欢迎访问Git常见指令工具箱</h2>
    <h2>你好:<%=username%> 同学</h2>
    <form action="http://www.Web2.com:8082?token=<%=token%>" method="post">
        <input type="submit" value="web2">
    </form>
    <form action="/logout?token=<%=token%>" method="post">
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

<!-- Git Commands Section -->
<div class="git-commands-container">
    <h3>常见 Git 指令</h3>
    <table class="git-commands-table">
        <tr>
            <th>Git 指令</th>
            <th>说明</th>
        </tr>
        <tr>
            <td>git init</td>
            <td>初始化一个 Git 仓库</td>
        </tr>
        <tr>
            <td>git clone &lt;url&gt;</td>
            <td>克隆一个远程仓库</td>
        </tr>
        <tr>
            <td>git add .</td>
            <td>添加所有更改的文件到暂存区</td>
        </tr>
        <tr>
            <td>git commit -m "message"</td>
            <td>提交更改，并附加提交说明</td>
        </tr>
        <tr>
            <td>git push</td>
            <td>将本地仓库的更改推送到远程仓库</td>
        </tr>
        <tr>
            <td>git pull</td>
            <td>从远程仓库拉取更新</td>
        </tr>
        <tr>
            <td>git status</td>
            <td>查看当前仓库的状态</td>
        </tr>
        <tr>
            <td>git log</td>
            <td>查看提交历史</td>
        </tr>
        <tr>
            <td>git branch</td>
            <td>查看或创建分支</td>
        </tr>
    </table>
</div>

</body>
</html>
