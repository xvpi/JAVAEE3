package com.lky.logincas.controller;

import com.lky.logincas.domain.User;
import com.lky.logincas.service.LoginHistoryService;
import com.lky.logincas.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/logout")
public class LogoutController extends HttpServlet {
    private final LoginHistoryService loginHistoryService = new LoginHistoryService();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String token = request.getParameter("token");
        HttpSession session = request.getSession();

        try {
            // 获取当前用户的信息（例如 userId）
            User user = UserService.getInstance().getUserByToken(token);
            if (user != null) {
                // 更新登录历史的登出时间
                loginHistoryService.recordLogout(user.getId());

                // 执行登出操作
                UserService.getInstance().logout(token);

                // 删除登录时设置的cookie
                Cookie cookieName = new Cookie("username", null);
                Cookie cookieToken = new Cookie("token", null);
                cookieName.setMaxAge(0);  // 设置为0表示删除cookie
                cookieToken.setMaxAge(0);  // 设置为0表示删除cookie
                response.addCookie(cookieName);
                response.addCookie(cookieToken);
            }
            // 重定向到登录页面
            response.sendRedirect("http://www.loginsystem.com:8081/jsp/login.jsp?web=main");
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "数据库错误");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }
}
