package com.oreo.finalproject_5re5_be.member.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class LoginAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        // 쿠키 처리
        handleSession(request, authentication);
        // 세션 처리
        handleCookie(request, response, authentication);
    }

    // 세션 등록
    private void handleSession(HttpServletRequest request, Authentication authentication) {
        String memberId = authentication.getName();
        HttpSession session = request.getSession();
        session.setAttribute("memberId", memberId);
    }

    // 쿠키 등록
    // 만약 쿠키 체크가 rememberMe로 되어 있다고 가정. 이 부분 추후에 프론트랑 얘기해야함
    private void handleCookie(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) {
        String memberId = authentication.getName();
        String rememberMe = request.getParameter("rememberMe");

        if (rememberMe != null) {
            Cookie cookie = new Cookie("memberId", memberId);
            cookie.setMaxAge(60 * 60 * 24 * 1); // 1일간 유지
            response.addCookie(cookie);
        } else {
            Cookie cookie = new Cookie("memberId", "");
            cookie.setMaxAge(0); // 쿠키 삭제
            response.addCookie(cookie);
        }
    }

}
