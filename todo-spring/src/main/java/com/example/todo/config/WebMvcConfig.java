package com.example.todo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration //스프링 빈으로 등록
public class WebMvcConfig implements WebMvcConfigurer{
	
	private final long MAX_AGE_SECS = 3600;
	
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		//모든 경로에 대해
		registry.addMapping("/**")
		//Origin이 http://localhost:3000에 대해.
		.allowedOrigins("http://localhost:3000",
				//"http://ec2-3-39-234-89.ap-northeast-2.compute.amazonaws.com:3000") //퍼블릭 IPv4 DNS(서버 끄기 전)p397
				//"http://ec2-3-37-129-103.ap-northeast-2.compute.amazonaws.com:3000") //퍼블릭 IPv4 DNS
				"http://ec2-3-34-137-84.ap-northeast-2.compute.amazonaws.com:3000") //퍼블릭 IPv4 DNS
		//GET, POST, PUT, PATCH, DELETE, OPTIONS 메서드를 이용한다.
		.allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
		.allowedHeaders("*")
		.allowCredentials(true)
		.maxAge(MAX_AGE_SECS);
	}
}
