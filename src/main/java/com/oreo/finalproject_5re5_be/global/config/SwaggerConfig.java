package com.oreo.finalproject_5re5_be.global.config;

import io.swagger.v3.oas.models.*;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.servers.Server;
import java.util.ArrayList;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        // swagger servers 지정
        List<Server> server = new ArrayList<>();
        server.add(new Server().description("https Server").url("https://dev1.5re5park.site"));
        server.add(new Server().description("localhost").url("http://localhost:8080"));

        OpenAPI info =
                new OpenAPI()
                        .components(new Components())
                        .paths(getPaths())
                        .servers(server)
                        .info(apiInfo());
        return info;
    }

    private Paths getPaths() {
        Paths paths = new Paths();

        // 로그인 API 경로 추가
        paths.addPathItem(
                "/api/member/login",
                new PathItem()
                        .post(
                                new Operation()
                                        .summary("회원 로그인")
                                        .description("스프링 시큐리티 기본 로그인 처리")
                                        .requestBody(
                                                new RequestBody()
                                                        .required(true)
                                                        .content(
                                                                new Content()
                                                                        .addMediaType(
                                                                                "application/x-www-form-urlencoded",
                                                                                new MediaType()
                                                                                        .schema(
                                                                                                new Schema<>()
                                                                                                        .addProperty("username", new StringSchema())
                                                                                                        .addProperty("password", new StringSchema())))))
                                        .responses(
                                                new ApiResponses()
                                                        .addApiResponse("200", new ApiResponse().description("로그인 성공"))
                                                        .addApiResponse("401", new ApiResponse().description("로그인 실패")))));

        // 로그아웃 API 경로 추가
        paths.addPathItem(
                "/api/member/logout",
                new io.swagger.v3.oas.models.PathItem()
                        .post(
                                new io.swagger.v3.oas.models.Operation()
                                        .summary("회원 로그아웃")
                                        .description("스프링 시큐리티 기본 로그아웃 처리")
                                        .responses(
                                                new ApiResponses()
                                                        .addApiResponse(
                                                                "200",
                                                                new ApiResponse()
                                                                        .description("로그아웃 성공")
                                                                        .content(
                                                                                new Content()
                                                                                        .addMediaType(
                                                                                                "application/x-www-form-urlencoded",
                                                                                                new MediaType()
                                                                                                        .schema(
                                                                                                                new Schema<>()
                                                                                                                        .type("string")
                                                                                                                        .example("Logout successful"))))))));

        return paths;
    }

    private Info apiInfo() {
        return new Info()
                .title("5re5 API Test") // API의 제목
                .description("swagger 5re5 park") // API에 대한 설명
                .version("1.0.0"); // API의 버전
    }
}
