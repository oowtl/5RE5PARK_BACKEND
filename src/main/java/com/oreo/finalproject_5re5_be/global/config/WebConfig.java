package com.oreo.finalproject_5re5_be.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOriginPatterns(
                        "https://*.5re5park.site",
                        "http://localhost:*",
                        "http://127.0.0.1:*"
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .exposedHeaders("location")
                .allowedHeaders("*")
                .allowCredentials(true);

        // Swagger를 위한 CORS 설정
        registry.addMapping("/swagger-ui/**")
                .allowedOriginPatterns(
                        "https://*.5re5park.site"
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*")
                .allowCredentials(false);
    }
}
