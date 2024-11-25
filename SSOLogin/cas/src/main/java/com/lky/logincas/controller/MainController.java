package com.lky.logincas.controller;

import com.lky.logincas.domain.User;
import com.lky.logincas.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.Objects;

@WebServlet("/jump")
public class MainController extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cookie[] cookies = ((HttpServletRequest) request).getCookies();
        String targetWeb = request.getParameter("web");
        String token = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("token".equals(cookie.getName())) {
                    token = cookie.getValue();
                }
            }
        }
        if(Objects.equals(targetWeb, "web1"))
            response.sendRedirect("http://www.CQU.com:8080?token=" + URLEncoder.encode(token, "UTF-8"));
        else{
            response.sendRedirect("http://www.PKU.com:8082?token=" + URLEncoder.encode(token, "UTF-8"));
        }
        return;
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }
}
