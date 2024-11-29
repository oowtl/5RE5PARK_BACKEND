package com.oreo.finalproject_5re5_be.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("https://client.5re5park.site")
                .allowedOrigins("https://5re5park.site")
                .allowedOrigins("https://www.5re5park.site")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .exposedHeaders("location")
                .allowedHeaders("*")
                .allowCredentials(false);
        // Swagger를 위한 CORS 설정
        registry.addMapping("/swagger-ui/**")
                .allowedOrigins("https://client.5re5park.site", "https://5re5park.site", "https://www.5re5park.site")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*")
                .allowCredentials(false);
    }
}
