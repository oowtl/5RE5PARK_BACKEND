package com.oreo.finalproject_5re5_be;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableRetry // 재시도 복구를 위한 어노테이션
@EnableScheduling // 스케줄링 처리를 위한 어노테이션
@OpenAPIDefinition(servers = {@Server(url = "https://5re5park.site", description = "5re5park site")})
@SpringBootApplication
public class FinalProject5Re5BeApplication {

    public static void main(String[] args) {
        SpringApplication.run(FinalProject5Re5BeApplication.class, args);
    }

}
