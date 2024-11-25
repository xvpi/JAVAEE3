package com.xvpi.cas.controller;


import com.xvpi.cas.domain.User;
import com.xvpi.cas.service.LoginHistoryService;
import com.xvpi.cas.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/login")
public class LoginController extends HttpServlet {
    private final LoginHistoryService loginHistoryService = new LoginHistoryService();
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String web = request.getParameter("web");
        if(web==null){web = "main";}
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        //创建JSON对象message，以便往前端响应信息
        HttpSession session = request.getSession(); // 创建session变量
        String message = null; // 定义message变量
        try{
            User loggedUser = UserService.getInstance().login(username, password);
            User existingUser = UserService.getInstance().findUserByUsername(username);
            if(loggedUser != null){
                String ipAddress = request.getRemoteAddr(); // 获取用户的 IP 地址
                String userAgent = request.getHeader("User-Agent"); // 获取用户设备信息
                loginHistoryService.recordLogin(loggedUser.getId(), loggedUser.getUsername(),
                        ipAddress,
                        userAgent);
                Cookie cookieName = new Cookie("username",loggedUser.getUsername());
                Cookie cookieToken = new Cookie("token",loggedUser.getToken());
                response.addCookie(cookieName);
                response.addCookie(cookieToken);
                //此处应重定向到索引页（菜单页）

                if(web==null)
                    response.sendRedirect("http://www.Cas.com:8081/jsp/main.jsp");
                else if(web.equals("web1"))
                    response.sendRedirect("http://www.Web1.com:8080?token="+loggedUser.getToken());
                else if(web.equals("web2"))
                    response.sendRedirect("http://www.Web2.com:8082/?token="+loggedUser.getToken());
                else{
                    response.sendRedirect("http://www.Cas.com:8081/jsp/main.jsp");
                }
                return;
            }else {
                // 检查用户状态是否正常
                if ("disabled".equals(existingUser.getStatus())) {
                    message = "您的账户已被禁用，请联系管理员恢复使用。";
                    session.setAttribute("message", message);
                }
                else{
                message = "未登录 或者 用户名或密码错误！";
                session.setAttribute("message", message);
                }
                response.sendRedirect("http://www.Cas.com:8081/jsp/login.jsp?web="+web);
            }
        }catch (SQLException e){
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=UTF-8");
            message="数据库操作异常";
            session.setAttribute("message", message);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }
}