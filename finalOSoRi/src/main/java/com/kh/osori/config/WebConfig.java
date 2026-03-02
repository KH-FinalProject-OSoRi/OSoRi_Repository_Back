package com.kh.osori.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
	    registry.addResourceHandler("/upload/badges/**")
	            .addResourceLocations("classpath:/static/upload/badges/");
	}


    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 모든 API 경로에 대해
                .allowedOrigins("http://localhost:5173",    // 로컬 테스트용
		                        "http://13.239.33.140",      // AWS 퍼블릭 IP (포트가 80이면 생략 가능)
		                        "http://13.239.33.140:5173") // 프론트 주소
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600); // 쿠키/인증 필요하면 유지 (JWT면 없어도 되긴 함)
    }
}
