package com.lky.logincas.controller;

import com.lky.logincas.domain.User;
import com.lky.logincas.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/register")
public class RegisterController extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String web = request.getParameter("web");

        try {
            // 检查用户名是否已经存在
            User existingUser = UserService.getInstance().findUserByUsername(username);
            if (existingUser != null) {
                // 如果用户名已存在，返回"username_exists"
                response.getWriter().write("username_exists");
                return;
            }

            // 如果用户名不存在，则进行注册
            User newUser = UserService.getInstance().register(username, password);
            if (newUser != null) {
                // 注册成功后返回"registration_success"，但不自动登录
                response.getWriter().write("registration_success");
            } else {
                // 注册失败，返回错误
                response.getWriter().write("registration_failed");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.getWriter().write("registration_failed");
        }
    }
}
