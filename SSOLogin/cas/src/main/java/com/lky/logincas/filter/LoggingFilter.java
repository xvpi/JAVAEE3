package com.lky.logincas.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class LoggingFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 初始化Filter，可以在这里进行一些配置
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        // 在请求到达Servlet之前执行的逻辑
        long startTime = System.currentTimeMillis();
        String requestURL = ((HttpServletRequest) request).getRequestURL().toString();
        System.out.println("Request received for URL: " + requestURL);

        // 继续处理请求
        chain.doFilter(request, response);

        // 在响应返回给客户端之前执行的逻辑
        long endTime = System.currentTimeMillis();
        System.out.println("Request for URL " + requestURL + " completed in " + (endTime - startTime) + " ms");
    }

    @Override
    public void destroy() {
        // Filter销毁时执行的逻辑
    }
}

