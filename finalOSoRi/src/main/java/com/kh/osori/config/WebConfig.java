package com.kh.osori.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
	    // 웹 호출 주소: /upload/badges/파일명
	    registry.addResourceHandler("/upload/badges/**")
	            .addResourceLocations("file:///C:/Users/user1/git/OSoRi_Repository_Back/finalOSoRi/upload/badges/"); 
	            // ↑ 마지막 슬래시(/)가 빠지면 폴더 안의 파일을 찾지 못합니다.
	}
}