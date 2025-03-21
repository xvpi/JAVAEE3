package com.xvpi.cas.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/clearcookie")
public class ClearCookie extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cookie[]cookies = request.getCookies();
        for(Cookie cookie:cookies){
            if("token".equals(cookie.getName())){
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            }
        }
        response.sendRedirect("http://www.Web2.com:8081/jsp/login.jsp?web=main");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }
}