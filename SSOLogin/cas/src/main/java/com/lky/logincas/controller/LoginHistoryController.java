package com.lky.logincas.controller;

import com.lky.logincas.domain.LoginHistory;
import com.lky.logincas.service.LoginHistoryService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/history")
public class LoginHistoryController extends HttpServlet {
    private final LoginHistoryService loginHistoryService = new LoginHistoryService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 设置默认值
        int page = 1; // 默认页码
        int pageSize = 10; // 默认每页条目数

        try {
            // 获取请求参数，若为 null 则使用默认值
            String pageParam = req.getParameter("page");
            String pageSizeParam = req.getParameter("pageSize");
            if (pageParam != null) {
                page = Integer.parseInt(pageParam);
            }
            if (pageSizeParam != null) {
                pageSize = Integer.parseInt(pageSizeParam);
            }

            // 获取登录历史列表
            List<LoginHistory> historyList = loginHistoryService.getLoginHistory(page, pageSize);
            req.setAttribute("historyList", historyList);
            req.setAttribute("currentPage", page);  // 设置当前页
            req.setAttribute("pageSize", pageSize);  // 设置每页条目数
            req.getRequestDispatcher("/jsp/history.jsp").forward(req, resp);
        } catch (NumberFormatException e) {
            // 参数格式错误
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid page or pageSize parameter");
        } catch (SQLException e) {
            // 数据库错误
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error");
        }
    }
}
