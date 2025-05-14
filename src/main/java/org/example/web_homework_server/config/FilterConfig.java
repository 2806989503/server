//package org.example.web_homework_server.config;
//
//import org.example.web_homework_server.Filter.a_LoginCheckFilter;
//import org.springframework.boot.web.servlet.FilterRegistrationBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class FilterConfig {
//
////    @Bean
////    public FileService fileService() {
////        return new FileServiceImpl();
////    }
//    @Bean
//    public FilterRegistrationBean<a_LoginCheckFilter> loginCheckFilterRegistration() {
//        FilterRegistrationBean<a_LoginCheckFilter> registration = new FilterRegistrationBean<>();
//        registration.setFilter(new a_LoginCheckFilter());
//        registration.addUrlPatterns("/files/*");
//        registration.setOrder(1); // 设置过滤器的顺序
//        return registration;
//    }
////
////    @Bean
////    public FilterRegistrationBean<b_FilePermissionsFilter> filePermissionsFilterRegistration(FileService fileService) {
////        FilterRegistrationBean<b_FilePermissionsFilter> registration = new FilterRegistrationBean<>();
////        b_FilePermissionsFilter filter = new b_FilePermissionsFilter(fileService);
////        registration.setFilter(filter);
////        registration.addUrlPatterns("/files/*");
//////        registration.setName("filePermissionsFilter");
//////        registration.setOrder(1);
////        return registration;
////    }
//}
