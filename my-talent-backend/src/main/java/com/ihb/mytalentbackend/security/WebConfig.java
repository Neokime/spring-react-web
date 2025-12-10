package com.ihb.mytalentbackend.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:5173")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }

    // ⭐ 여기 추가
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String projectRoot = System.getProperty("user.dir"); // 현재 프로젝트 루트
        String uploadPath = projectRoot + "/uploads/";       // 이미 파일이 들어가는 폴더

        registry.addResourceHandler("/uploads/**")           // 브라우저에서 요청하는 URL
                .addResourceLocations("file:" + uploadPath); // 실제 로컬 경로
    }
}
