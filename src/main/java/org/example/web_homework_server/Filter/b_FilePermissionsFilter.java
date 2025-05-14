//package org.example.web_homework_server.Filter;
//
//import jakarta.servlet.*;
//import jakarta.servlet.annotation.WebFilter;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.extern.slf4j.Slf4j;
//import org.example.web_homework_server.b_service.FileService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//
//@Slf4j
//public class b_FilePermissionsFilter implements Filter {
//
//    private final FileService fileService;
//
//    // 通过构造器注入服务
//    public b_FilePermissionsFilter(FileService fileService) {
//        this.fileService = fileService;
//    }
//
//    @Override
//    public void doFilter(ServletRequest servletRequest,
//                         ServletResponse servletResponse,
//                         FilterChain filterChain) throws IOException, ServletException {
//
//        HttpServletRequest request = (HttpServletRequest) servletRequest;
//        HttpServletResponse response = (HttpServletResponse) servletResponse;
//
//
//        String username = (String) request.getAttribute("username");
//        String url = request.getRequestURI();
//        //url在请求体中，无法读取
//
//        // 检查用户是否有权限访问该文件
//        if (!fileService.hasPermission(username, url)) {
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            response.getWriter().write("拒绝访问：权限不足");
//            return; // 终止过滤器链
//        }
//
//        // 用户有权限，继续过滤器链
//        System.out.println("文件放行");
//        filterChain.doFilter(request, response);
//    }
//
//    @Override
//    public void init(FilterConfig filterConfig) throws ServletException {
//        // 初始化代码（如果需要）
//    }
//
//    @Override
//    public void destroy() {
//        // 清理代码（如果需要）
//    }
//}
