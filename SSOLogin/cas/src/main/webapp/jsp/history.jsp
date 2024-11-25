<%@ page import="com.lky.logincas.service.LoginHistoryService" %>
<%@ page import="java.util.List" %>
<%@ page import="com.lky.logincas.domain.LoginHistory" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>历史记录</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
            background-color: #f2f2f2;
        }

        h1 {
            color: #333;
            text-align: center;
        }

        .history-container {
            width: 80%;
            margin: 50px auto;
            background-color: #fff;
            box-shadow: 0px 0px 10px 0px rgba(0, 0, 0, 0.1);
            padding: 20px;
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

        .pagination {
            text-align: center;
            margin-top: 20px;
        }

        .pagination a {
            margin: 0 5px;
            text-decoration: none;
            color: #333;
            padding: 8px 16px;
            border: 1px solid #ddd;
            border-radius: 5px;
        }

        .pagination a.active {
            background-color: #4CAF50;
            color: white;
        }
    </style>
</head>
<body>
<div class="history-container">
    <h1>用户登录历史记录</h1>

    <table>
        <tr>
            <th>用户ID</th>
            <th>用户名</th>
            <th>登录时间</th>
            <th>登出时间</th>
            <th>IP 地址</th>
            <th>设备信息</th>
        </tr>

        <%
            // 从 request 获取 "historyList" 属性
            List<LoginHistory> historyList = (List<LoginHistory>) request.getAttribute("historyList");
            for (LoginHistory history : historyList) {
        %>
        <tr>
            <td><%= history.getUserId() %></td>
            <td><%= history.getUsername() %></td>
            <td><%= history.getLoginTime() %></td>
            <td><%= history.getLogoutTime() != null ? history.getLogoutTime() : "未登出" %></td>
            <td><%= history.getIpAddress() %></td>
            <td><%= history.getDeviceInfo() %></td>
        </tr>
        <% } %>
    </table>

    <div class="pagination">
        <%
            // 获取当前页面和每页显示条数
            Integer currentPage = (Integer) request.getAttribute("currentPage");
            Integer pageSize = (Integer) request.getAttribute("pageSize");

            int totalRecords = LoginHistoryService.getTotalRecordCount();  // 你需要通过服务层获取总记录数
            int totalPages = (int) Math.ceil(totalRecords / (double) pageSize);

            if (currentPage > 1) {
        %>
        <a href="history.jsp?page=<%= currentPage - 1 %>">&laquo; 上一页</a>
        <%
            }
            for (int i = 1; i <= totalPages; i++) {
        %>
        <a href="history.jsp?page=<%= i %>" class="<%= i == currentPage ? "active" : "" %>"><%= i %></a>
        <%
            }
            if (currentPage < totalPages) {
        %>
        <a href="history.jsp?page=<%= currentPage + 1 %>">下一页 &raquo;</a>
        <%
            }
        %>
    </div>
</div>
</body>
</html>
