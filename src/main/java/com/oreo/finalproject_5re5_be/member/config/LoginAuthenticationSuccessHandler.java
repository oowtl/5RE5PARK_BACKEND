package com.oreo.finalproject_5re5_be.member.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oreo.finalproject_5re5_be.member.dto.CustomUserDetails;
import com.oreo.finalproject_5re5_be.member.entity.Member;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

// 로그인 성공시 처리되는 핸들러
@Slf4j
@Component
public class LoginAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        // 로그인 성공시 유저 정보 반환
        Object principal = authentication.getPrincipal();
        Map<String, Object> memberInfo = new HashMap<>();

        String memberId = "";
        Long memberSeq = 0L;

        if (principal instanceof CustomUserDetails) {
            CustomUserDetails memberDetails = (CustomUserDetails) principal;
            Member member = memberDetails.getMember();

            memberInfo.put("seq", member.getSeq());
            memberInfo.put("id", member.getId());
            memberInfo.put("name", member.getName());
            memberInfo.put("email", member.getEmail());

            memberId = member.getId();
            memberSeq = member.getSeq();

        } else if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;

            memberInfo.put("username", userDetails.getUsername());

            memberId = userDetails.getUsername();
            memberSeq = 0L;
        }

        log.info("memberSeq = {}", memberSeq);
        log.info("memberId = {}", memberId);

        // 기존 세션 삭제
        request.getSession().invalidate();

        // 새로운 세션 생성
        HttpSession session = request.getSession(true);
        session.setAttribute("memberId", memberId);
        session.setAttribute("memberSeq", memberSeq);

        // 세션 ID를 쿠키로 설정
        setSessionCookie(request, response, session.getId());

        // JSON 응답
        response.setContentType("application/json;charset=UTF-8");
        new ObjectMapper().writeValue(response.getWriter(), memberInfo);
    }

    /**
     * 세션 쿠키 설정 (포트 간 전달 가능하게 설정)
     */
    private void setSessionCookie(HttpServletRequest request, HttpServletResponse response, String sessionId) {
        Cookie sessionCookie = new Cookie("SESSIONID", sessionId);

        // 도메인 설정
        String domain = request.getServerName();
        if (!"localhost".equals(domain)) {
            sessionCookie.setDomain(domain); // 실제 도메인 사용
        }

        sessionCookie.setHttpOnly(true);
        sessionCookie.setSecure(!"localhost".equals(domain)); // 로컬에서는 Secure 비활성화
        sessionCookie.setPath("/"); // 모든 경로에서 사용 가능
        sessionCookie.setMaxAge(-1); // 세션이 종료될 때 삭제

        // SameSite 설정 (Set-Cookie 헤더로 처리)
        String cookieHeader = String.format("%s=%s; Path=%s; HttpOnly; Secure; SameSite=None",
                sessionCookie.getName(), sessionCookie.getValue(), sessionCookie.getPath());
        response.setHeader("Set-Cookie", cookieHeader);

        log.info("Set-Cookie Header: {}", cookieHeader);
    }

}
