//package com.kh.osori.config;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.springframework.web.servlet.HandlerInterceptor;
//
//import com.kh.osori.util.JwtUtil;
//
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//
//@Component 
//public class LoginInterceptor implements HandlerInterceptor {
//
//    @Autowired
//    private JwtUtil jwtUtil; 
//
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//    	
//    	if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
//            return true;
//        }
//    	
//        // 헤더에서 토큰 꺼내기
//        String authHeader = request.getHeader("Authorization");
//        System.out.println("받은 헤더: " + authHeader);
//        
//        if (authHeader != null && authHeader.startsWith("Bearer ")) {
//            String token = authHeader.substring(7); 
//            System.out.println("추출된 토큰: " + token);
//            try {
//                if (jwtUtil.validateToken(token)) {
//                	String loginId = jwtUtil.getLoginIdFromToken(token); 
//                 request.setAttribute("loginId", loginId);
//                    
//                    return true; 
//                }
//            } catch (Exception e) {
//                System.out.println("토큰 검증 실패: " + e.getMessage());
//            }
//        }
//        
//        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//        return false;
//    }
//}