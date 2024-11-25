package com.lky.logincas.controller;


import com.alibaba.fastjson.JSONObject;
import com.lky.logincas.domain.User;
import com.lky.logincas.service.LoginHistoryService;
import com.lky.logincas.service.UserService;
import com.lky.logincas.util.GenerateToken;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
/*

用户认证生成token的过程通常如下：

用户向服务器发送登录请求，这通常包括用户名和密码。
服务器验证用户名和密码。如果验证成功，服务器会生成一个token。
服务器将生成的token发送回用户。
用户收到token后，将其存储在本地，例如在cookie中。
当用户需要访问需要认证的资源时，他们会将token一起发送给服务器。
服务器收到请求后，会验证token的有效性。如果token有效，服务器会处理请求并返回响应。
 */

@WebServlet("/login")
public class LoginController extends HttpServlet {
    private final LoginHistoryService loginHistoryService = new LoginHistoryService();
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String web = request.getParameter("web");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        //创建JSON对象message，以便往前端响应信息
        HttpSession session = request.getSession(); // 创建session变量
        String message = null; // 定义message变量
        try{
            User loggedUser = UserService.getInstance().login(username, password);
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
                    response.sendRedirect("http://www.LoginSystem.com:8081/jsp/main.jsp");
                else if(web.equals("web1"))
                    response.sendRedirect("http://www.CQU.com:8080?token="+loggedUser.getToken());
                else if(web.equals("web2"))
                    response.sendRedirect("http://www.PKU.com:8082/?token="+loggedUser.getToken());
                else{
                    response.sendRedirect("http://www.LoginSystem.com:8081/jsp/main.jsp");
                }
                return;
            }else {
                message = "未登录 或者 用户名或密码错误！";
                session.setAttribute("message", message);
                response.sendRedirect("http://www.LoginSystem.com:8081/jsp/login.jsp");
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