package com.oreo.finalproject_5re5_be.global.config;

import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.headers.Header;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;

import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        OpenAPI info = new OpenAPI()
                .components(new Components())
                .paths(getPaths())
                .info(apiInfo());
        return info;
    }

    private Paths getPaths() {
        Paths paths = new Paths();

        // 로그인 API 경로 추가
        paths.addPathItem("/api/member/login", new PathItem()
                .post(new Operation()
                        .summary("회원 로그인")
                        .description("스프링 시큐리티 기본 로그인 처리 - 로그인 성공 시 메인 페이지로 리다이렉션")
                        .addParametersItem(new Parameter()
                                .name("username")
                                .description("사용자 아이디")
                                .required(true)
                                .in("query")
                                .schema(new Schema<>().type("string"))
                        )
                        .addParametersItem(new Parameter()
                                .name("password")
                                .description("사용자 비밀번호")
                                .required(true)
                                .in("query")
                                .schema(new Schema<>().type("string"))
                        )
                        .responses(new ApiResponses()
                                .addApiResponse("200", new ApiResponse()
                                        .description("로그인 성공")
                                        .content(new Content()
                                                .addMediaType("application/json", new MediaType()
                                                        .schema(new Schema<>().type("string"))))
                                )
                                .addApiResponse("302", new ApiResponse()
                                        .description("로그인 성공 후 메인 페이지로 리다이렉션")
                                )
                                .addApiResponse("401", new ApiResponse()
                                        .description("로그인 실패 - 잘못된 자격 증명")
                                )
                        )
                )
        );

        // 로그아웃 API 경로 추가
        paths.addPathItem("/api/member/logout", new io.swagger.v3.oas.models.PathItem()
                .post(new io.swagger.v3.oas.models.Operation()
                        .summary("회원 로그아웃")
                        .description("스프링 시큐리티 기본 로그아웃 처리")
                        .responses(new ApiResponses()
                                .addApiResponse("200", new ApiResponse()
                                        .description("로그아웃 성공")
                                        .content(new Content()
                                                .addMediaType("application/json", new MediaType()
                                                        .schema(new Schema<>().type("string").example("Logout successful"))))
                                )
                        )
                )
        );

        return paths;
    }



    private Info apiInfo() {
        return new Info()
                .title("API Test") // API의 제목
                .description("Let's practice Swagger UI") // API에 대한 설명
                .version("1.0.0"); // API의 버전
    }
}