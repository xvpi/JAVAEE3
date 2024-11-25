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

@WebServlet("/ShowAllUser")
public class ShowAllUser extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String userList = UserService.getInstance().showAllUser().toString();
            response.getWriter().write(userList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }
}