package com.oreo.finalproject_5re5_be.global.config;

import jakarta.servlet.http.HttpSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
public class JpaAuditingConfig {
    // JPA Auditing Bean 등록
    @Bean
    public AuditorAware<Long> auditorProvider(HttpSession session) {
        return new AuditorAwareImpl(session);
    }
}
