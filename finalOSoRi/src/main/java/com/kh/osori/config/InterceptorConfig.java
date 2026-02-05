//package com.kh.osori.config;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//public class InterceptorConfig implements WebMvcConfigurer {
//
//    @Autowired
//    private LoginInterceptor loginInterceptor;
//
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(loginInterceptor)
//                .addPathPatterns("/challenges/**", "/fixedtrans/**", "/user/**")
//                .excludePathPatterns(
//                    "/challenges", 
//                    "/user/login", 
//                    "/user/register", 
//                    "/user/logout", 
//                    "/user/checkId",
//                    "/user/checkNickName", 
//                    "/user/checkEmail", 
//                    "/user/findId", 
//                    "/user/findPassword",
//                    "/user/resetPassword", 
//                    "/user/kakao/callback",
//                    "/ws/**",
//                    "/group/**", 
//                    "/trans/**",
//                    "/osori/ws/**"
//                );
//    }
//    
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowedOrigins("http://localhost:5173")
//                .allowedMethods("*")
//                .allowedHeaders("*")
//                .allowCredentials(true);
//    }
//}
