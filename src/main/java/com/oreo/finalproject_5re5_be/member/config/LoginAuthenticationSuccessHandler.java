package com.oreo.finalproject_5re5_be.member.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oreo.finalproject_5re5_be.member.dto.CustomUserDetails;
import com.oreo.finalproject_5re5_be.member.entity.Member;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

// 로그인 성공시 처리되는 핸들러
@Slf4j
@Component
public class LoginAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        log.info("[LoginAuthenticationSuccessHandler] Login successful - Authentication: {}", authentication);

        // 사용자 인증 정보 추출
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Member member = userDetails.getMember();

        log.info("[LoginAuthenticationSuccessHandler] Member details: {}", member);

        // 세션 생성 또는 기존 세션 반환
        HttpSession session = request.getSession(true); // true: 세션이 없으면 생성
        String sessionId = session.getId();
        session.setAttribute("memberSeq", member.getSeq());
        session.setAttribute("memberId", member.getId());
        log.info("[LoginAuthenticationSuccessHandler] Session created - ID: {}", sessionId);

        // 쿠키 설정
        handleCookie(response, sessionId);

        // 사용자 정보 응답 생성
        Map<String, Object> memberInfo = new HashMap<>();
        memberInfo.put("memberSeq", member.getSeq());
        memberInfo.put("memberId", member.getId());
        memberInfo.put("name", member.getName());
        memberInfo.put("email", member.getEmail());

        response.setContentType("application/json;charset=UTF-8");
        new ObjectMapper().writeValue(response.getWriter(), memberInfo);

        log.info("[LoginAuthenticationSuccessHandler] Login response sent successfully");
    }

    private void handleCookie(HttpServletResponse response, String sessionId) {
        Cookie sessionCookie = new Cookie("SESSIONID", sessionId);

        // 쿠키 보안 설정
        sessionCookie.setHttpOnly(true);
        sessionCookie.setSecure(true); // HTTPS 환경에서만 전송
        sessionCookie.setPath("/");   // 모든 경로에서 유효
        sessionCookie.setMaxAge(-1);  // 브라우저 종료 시 삭제

        // 쿠키 추가
        response.addCookie(sessionCookie);

        // SameSite=None을 추가한 쿠키 헤더
        String cookieHeader = String.format("%s=%s; Path=%s; HttpOnly; Secure; SameSite=None",
                sessionCookie.getName(), sessionCookie.getValue(), sessionCookie.getPath());
        response.setHeader("Set-Cookie", cookieHeader);

        log.info("[handleCookie] SESSIONID cookie set: {}", sessionCookie.getName());
    }

}
