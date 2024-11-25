package com.lky.logincas.controller;

import com.lky.logincas.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/user-management")
public class UserManagementController extends HttpServlet {
    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            req.setAttribute("users", userService.getAllUsers());
            req.getRequestDispatcher("/jsp/user-management.jsp").forward(req, resp);
        } catch (SQLException e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userIdStr = request.getParameter("userId");
        String action = request.getParameter("action");

        if (userIdStr == null || action == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid request parameters");
            return;
        }

        try {
            int userId = Integer.parseInt(userIdStr);
            userService.toggleUserStatus(userId, action);
            response.sendRedirect("/user-management");  // 更新后跳转回用户管理页面
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error processing request");
        }
    }
}

