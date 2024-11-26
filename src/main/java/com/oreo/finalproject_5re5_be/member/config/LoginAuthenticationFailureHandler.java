package com.oreo.finalproject_5re5_be.member.config;


import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

// 로그인 성공시 처리되는 핸들러
@Component
public class LoginAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception) throws IOException, ServletException {
        // 로그인 실패시 처리되는 핸들러
        // 로그인 실패시 로그인 페이지로 이동
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
}
