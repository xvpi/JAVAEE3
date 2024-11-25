<%@ page import="com.lky.logincas.service.UserService" %>
<%@ page import="com.lky.logincas.domain.User" %>
<%@ page import="java.util.List" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>用户管理</title>
  <style>
    body {
      font-family: Arial, sans-serif;
      margin: 0;
      background-image: url("../image/cqu_bg.png");
      padding: 0;
    }

    h1 {
      color: #333;
      text-align: center;
    }

    .management-container {
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

    .action-button {
      padding: 5px 10px;
      border: none;
      border-radius: 3px;
      cursor: pointer;
    }

    .disable {
      background-color: #f44336;
      color: white;
    }

    .enable {
      background-color: #4CAF50;
      color: white;
    }

    .back-button {
      display: block;
      width: 150px;
      margin: 20px auto;
      padding: 10px;
      text-align: center;
      background-color: #2196F3;
      color: white;
      text-decoration: none;
      border-radius: 5px;
    }

    .back-button:hover {
      background-color: #0b7dda;
    }
  </style>
</head>
<body>
<div class="management-container">
  <h1>用户管理</h1>

  <table>
    <tr>
      <th>用户ID</th>
      <th>用户名</th>
      <th>状态</th>
      <th>操作</th>
    </tr>

    <%
      // 获取用户列表
      UserService userService = new UserService();
      List<User> userList = userService.getAllUsers();

      for (User user : userList) {
    %>
    <tr>
      <td><%= user.getId() %></td>
      <td><%= user.getUsername() %></td>
      <td><%= "normal".equals(user.getStatus()) ? "正常" : "禁用" %></td>
      <td>
        <form action="/user-management" method="post" style="display: inline;">
          <input type="hidden" name="userId" value="<%= user.getId() %>">
          <input type="hidden" name="action" value="<%= "normal".equals(user.getStatus()) ? "disable" : "enable" %>">
          <button type="submit"
                  class="action-button <%= "normal".equals(user.getStatus()) ? "disable" : "enable" %>">
            <%= "normal".equals(user.getStatus()) ? "禁用" : "恢复" %>
          </button>
        </form>
      </td>
    </tr>
    <% } %>
  </table>

  <!-- 返回按钮 -->
  <a href="http://www.LoginSystem.com:8081/jsp/main.jsp" class="back-button">返回首页</a>
</div>
</body>
</html>
