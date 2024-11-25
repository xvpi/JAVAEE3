<%@ page import="java.net.URL" %>
<%@ page import="java.net.HttpURLConnection" %>
<%@ page import="java.io.BufferedReader" %>
<%@ page import="java.io.InputStreamReader" %>
<%@ page import="java.io.OutputStream" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    String token = null;
    if (request.getQueryString() == null) {
//        先检测访问的有没有给token，没有就要检测cookie
        Cookie[] cookies = ((HttpServletRequest) request).getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("token".equals(cookie.getName())) {
                    token = cookie.getValue();
                }
            }
        }
    } else {
        token = request.getQueryString().split("=")[1];
    }
    if (token == null|| token.isEmpty()) {
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
        }
        // 关闭连接
        connection.disconnect();
        response.sendRedirect("http://www.Web2.com:8082/jsp/Web2MainPage.jsp");
    }
%>