package com.oreo.finalproject_5re5_be.member.config;

import java.util.List;
import org.apache.catalina.filters.CorsFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import org.springframework.web.cors.CorsConfigurationSource;


@Configuration
@EnableWebSecurity
public class MemberSecurityConfig {

    private final LoginAuthenticationSuccessHandler successHandler;
    private final CorsConfigurationSource corsConfigurationSource;

    public MemberSecurityConfig(LoginAuthenticationSuccessHandler successHandler, CorsConfigurationSource corsConfigurationSource) {
        this.successHandler = successHandler;
        this.corsConfigurationSource = corsConfigurationSource;
    }

    // SecurityFilterChain 설정 빈 등록, 추후에 적용 예정(다른 파트 작업 완료후 인증/인가 처리 적용예정)
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(csrf -> csrf.disable()) // csrf 비활성화
//                .authorizeHttpRequests(authorize -> authorize
//                        .requestMatchers("/", "/api/member/register") // 회원가입 페이지와 홈 페이지는 인증/인가 없이 접근 가능
//                        .permitAll()
//                        .requestMatchers("/audio/**", "/project/**", "/languagecode/**",
//                                "/voice/**", "/style", "/vc/**", "/concat") // 그외의 페이지는 인증/인가가 필요함
//                        .authenticated()
//                )
//                .formLogin(formLogin -> formLogin
//                        .loginPage("/api/member/login") // 로그인 페이지 경로 설정
//                        .successHandler(successHandler) // 로그인 성공시 처리되는 핸들러 설정
//                        .failureUrl("/api/member/login") // 로그인 실패시 로그인 페이지로 이동
//                ).logout(logout -> logout
//                        .logoutUrl("/api/member/logout") // 로그아웃 경로 설정
//                        .invalidateHttpSession(true) // 로그아웃시 세션 무효화 설정
//                        .logoutSuccessUrl("/") // 로그아웃 성공시 이동할 페이지 설정
//                );
//
//
//        return http.build();
//    }
// 밑에 개발 어느정도 마무리 되면 주석처리된 부분 주석 해제하고 적용할 예정
//                        .requestMatchers("/", "/member/**", "/api/member/**", "/api/member-term-condition/**", // 허용되는 URL
//                                         "/audio/**", "/project/**", "/languagecode/**",
//                                         "/voice/**", "/style/**", "/vc/**", "/concat/**",
//                                         "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html") // Swagger 관련 URL 허용


    // SecurityFilterChain을 빈으로 등록
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource)) // 새로운 방식으로 CORS 설정 적용
                .csrf(csrf -> csrf.disable()) // CSRF 비활성화
                .authorizeHttpRequests(authorize -> authorize
                                .anyRequest() // 개발 단계로 모든 요청 열어둠
                                .permitAll() // 위 URL들은 인증 없이 접근 가능
                )
                .formLogin(formLogin -> formLogin
                        .loginPage("/api/member/login") // 로그인 페이지 경로 설정
                        .successHandler(successHandler) // 로그인 성공 시 처리되는 핸들러
                        .failureUrl("/api/member/login") // 로그인 실패 시 로그인 페이지로 이동
                )
                .logout(logout -> logout
                        .logoutUrl("/api/member/logout") // 로그아웃 경로 설정
                        .invalidateHttpSession(true) // 로그아웃 시 세션 무효화
                        .logoutSuccessUrl("/") // 로그아웃 성공 시 이동할 페이지
                );

        return http.build();
    }

}
