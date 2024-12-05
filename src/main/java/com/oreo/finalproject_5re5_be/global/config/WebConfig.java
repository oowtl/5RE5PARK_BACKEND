package com.oreo.finalproject_5re5_be.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(
                        "https://client.5re5park.site",
                        "https://5re5park.site",
                        "https://www.5re5park.site",
                        "http://5re5park.site:8080",
                        "http://localhost:8080",
                        "http://localhost:5173",
                        "http://127.0.0.1:8080",
                        "http://127.0.0.1:5173") // 정확한 도메인 설정
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowCredentials(true) // 쿠키와 인증 정보 허용
                .maxAge(3600);
    }
}
