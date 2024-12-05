package com.oreo.finalproject_5re5_be.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns(
                        "https://*.5re5park.site", // 모든 서브도메인 허용
                        "http://localhost:*",      // localhost의 모든 포트 허용
                        "http://127.0.0.1:*"       // 127.0.0.1의 모든 포트 허용
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true) // 쿠키와 인증 정보 허용
                .exposedHeaders("*") // 클라이언트가 읽을 수 있는 헤더
                .maxAge(3600);
    }
}
