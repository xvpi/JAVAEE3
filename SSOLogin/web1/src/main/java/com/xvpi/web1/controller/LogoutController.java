package com.xvpi.web1.controller;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

@WebServlet("/logout")
public class LogoutController extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String targetUrl = "http://www.Cas.com:8081/logout";
        String token = request.getParameter("token");
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
            Cookie cookieToken = new Cookie("token", null);
            response.addCookie(cookieToken);
            Cookie cookieUsername = new Cookie("username", null);
            response.addCookie(cookieUsername);
        }
        // 关闭连接
        connection.disconnect();
        response.sendRedirect("http://www.Web2.com:8081/jsp/login.jsp?logout=true");

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request,response);
    }
}