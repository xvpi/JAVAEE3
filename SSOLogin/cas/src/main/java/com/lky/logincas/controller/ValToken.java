package com.lky.logincas.controller;

import com.alibaba.fastjson.JSONObject;
import com.lky.logincas.domain.User;
import com.lky.logincas.service.UserService;
import com.lky.logincas.util.GenerateToken;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/ValToken")
public class ValToken extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String token = request.getParameter("token");
        String username = null;
        String valSuccess = "false";
        String loginTime = null;
        try {
            if (UserService.getInstance().valToken(token) != null) {
                User user = UserService.getInstance().valToken(token);
                username = user.getUsername();
                loginTime = user.getLoginTime();
                valSuccess = "true";
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        response.getWriter().write(valSuccess + ";");
        response.getWriter().write(username + ";");
        response.getWriter().write(loginTime);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }
}

