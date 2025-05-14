//package org.example.web_homework_server.Filter;
//
//import com.alibaba.fastjson.JSONObject;
//import io.jsonwebtoken.Claims;
//import jakarta.servlet.*;
//import jakarta.servlet.annotation.WebFilter;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.extern.slf4j.Slf4j;
//import org.example.web_homework_server.pojo.Result;
//import org.example.web_homework_server.util.JwtUtils;
//import org.springframework.core.annotation.Order;
//import org.springframework.util.StringUtils;
//import org.springframework.web.bind.annotation.CrossOrigin;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.util.Enumeration;
//import java.util.Map;
//
//@Slf4j
////@WebFilter(urlPatterns = "/files")
////@Order(1)
//public class a_LoginCheckFilter implements Filter {
//
//    @Override
//    public void init(FilterConfig filterConfig) throws ServletException {
//        Filter.super.init(filterConfig);
//    }
//
//    @Override
//    public void doFilter(
//            ServletRequest servletRequest,
//            ServletResponse servletResponse,
//            FilterChain filterChain
//    ) throws IOException, ServletException {
//
//        log.info("拦截器捕获");
//
//        HttpServletRequest request = (HttpServletRequest) servletRequest;
//        HttpServletResponse response = (HttpServletResponse) servletResponse;
//
//        //获取url
//        String url = request.getRequestURI();
//        log.info("url. "+url);
//
//
//        //判断是否是登录
//        if(url.contains("/login")) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//
////        // 打印所有请求头
////        Enumeration<String> headerNames = request.getHeaderNames();
////        while (headerNames.hasMoreElements()) {
////            String headerName = headerNames.nextElement();
////            String headerValue = request.getHeader(headerName);
////            log.info("Header: {} = {}", headerName, headerValue);
////        }
////
////        // 打印所有请求参数
////        Map<String, String[]> parameterMap = request.getParameterMap();
////        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
////            String paramName = entry.getKey();
////            String[] paramValues = entry.getValue();
////            log.info("Parameter: {} = {}", paramName, String.join(", ", paramValues));
////        }
////
////        // 打印请求体
////        StringBuilder requestBody = new StringBuilder();
////        BufferedReader reader = request.getReader();
////        char[] buff = new char[1024];
////        int len;
////        while ((len = reader.read(buff)) != -1) {
////            requestBody.append(buff, 0, len);
////        }
////        log.info("Request Body: {}", requestBody.toString());
//
//
//        // 处理 CORS 预检请求 (OPTIONS)
//        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
//            response.setHeader("Access-Control-Allow-Origin", "*");
//            response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
//            response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization, token");
//            response.setHeader("Access-Control-Allow-Credentials", "true");
//            response.setStatus(HttpServletResponse.SC_OK);
//            return;
//        }
//
//        //获取令牌
//        String jwt = request.getHeader("token");
//
//
//        //判断令牌是否存在
//        if(!StringUtils.hasLength(jwt)){
//            log.info("请求头为空");
//            Result error = Result.error("令牌不存在！");
//            //手动对象转json
//            String notLogin = JSONObject.toJSONString(error);
//            response.getWriter().write(notLogin);
//            return;
//        }
//
//
//        // 校验令牌并解析用户名
//        String username;
//        try {
//            jwtUtils.parseJwt(jwt);
//            username = jwtUtils.getUsernameFromJwt(jwt);
//        } catch (Exception e) {
//            e.printStackTrace();
//            log.info("解析令牌失败");
//            Result error = Result.error("NOT_LOGIN");
//            String notLogin = JSONObject.toJSONString(error);
//            response.getWriter().write(notLogin);
//            return;
//        }
//
//        // 将用户名添加到请求属性
//        request.setAttribute("username", username);
//
//
//        //放行
//        System.out.println("登录放行");
//        filterChain.doFilter(request, response);
//
//    }
//
//    @Override
//    public void destroy() {
//        Filter.super.destroy();
//    }
//}
