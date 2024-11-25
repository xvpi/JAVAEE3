package com.xvpi.cas.controller;

import com.xvpi.cas.domain.User;
import com.xvpi.cas.service.UserService;

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

