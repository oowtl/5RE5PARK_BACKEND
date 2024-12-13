package com.oreo.finalproject_5re5_be.member.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oreo.finalproject_5re5_be.member.dto.CustomUserDetails;
import com.oreo.finalproject_5re5_be.member.entity.Member;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

// 로그인 성공시 처리되는 핸들러
@Slf4j
@Component
public class LoginAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException {
        log.info(
                "[LoginAuthenticationSuccessHandler] onAuthenticationSuccess - request : {} -> {} -> {}",
                request.toString(),
                response.toString(),
                authentication.toString());
        // 로그인 성공시 유저 정보 반환
        // 사용자 정보 추출
        Object principal = authentication.getPrincipal();
        log.info(
                "[LoginAuthenticationSuccessHandler] onAuthenticationSuccess - principal : {} ", principal);
        Map<String, Object> memberInfo = new HashMap<>();

        String memberId = "";
        Long memberSeq = 0L;

        if (principal instanceof CustomUserDetails) {
            log.info(
                    "[LoginAuthenticationSuccessHandler] onAuthenticationSuccess - principal : {} ",
                    principal);
            CustomUserDetails memberDetails = (CustomUserDetails) principal;
            log.info(
                    "[LoginAuthenticationSuccessHandler] onAuthenticationSuccess - memberDetails : {} ",
                    memberDetails);
            Member member = memberDetails.getMember();
            log.info(
                    "[LoginAuthenticationSuccessHandler] onAuthenticationSuccess - member : {} ",
                    member.toString());
            memberInfo.put("seq", member.getSeq());
            memberInfo.put("id", member.getId());
            memberInfo.put("name", member.getName());
            memberInfo.put("email", member.getEmail());

            memberId = member.getId();
            memberSeq = member.getSeq();

        } else if (principal instanceof UserDetails) {
            log.info(
                    "[LoginAuthenticationSuccessHandler] onAuthenticationSuccess - principal : {} ",
                    principal);
            UserDetails userDetails = (UserDetails) principal;
            log.info(
                    "[LoginAuthenticationSuccessHandler] onAuthenticationSuccess - userDetails : {} ",
                    userDetails);

            memberInfo.put("username", userDetails.getUsername());

            memberId = userDetails.getUsername();
            log.info(
                    "[LoginAuthenticationSuccessHandler] onAuthenticationSuccess - memberId = : {} ",
                    memberId);
            memberSeq = 0L;
        }

        log.info("memberSeq = {}", memberSeq);
        log.info("memberId = {}", memberId);

        // 세션 조회
        HttpSession session = request.getSession(true);
        log.info(
                "[LoginAuthenticationSuccessHandler] onAuthenticationSuccess - session = : {} ",
                session.toString());

        // 세션에 아이디 등록
        session.setAttribute("memberId", memberId);
        log.info(
                "[LoginAuthenticationSuccessHandler] onAuthenticationSuccess - session-memberId = : {} ",
                session.getAttribute("memberId"));
        session.setAttribute("memberSeq", memberSeq);
        log.info(
                "[LoginAuthenticationSuccessHandler] onAuthenticationSuccess - session-memberSeq = : {} ",
                session.getAttribute("memberSeq"));
        // 로그인 성공 후 context 등록
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 세션 처리
        handleCookie(request, response, authentication);

        // JSON으로 응답
        response.setContentType("application/json;charset=UTF-8");
        log.info(
                "[LoginAuthenticationSuccessHandler] onAuthenticationSuccess - response : {} ", response);
        new ObjectMapper().writeValue(response.getWriter(), memberInfo);
    }

    // 쿠키 등록
    // 만약 쿠키 체크가 rememberMe로 되어 있다고 가정. 이 부분 추후에 프론트랑 얘기해야함
    private void handleCookie(
            HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        log.info(
                "[LoginAuthenticationSuccessHandler] handleCookie - request : {} -> {} -> {}",
                request.toString(),
                response.toString(),
                authentication.toString());
        // Authentication에서 회원 아이디 조회
        String memberId = authentication.getName();
        log.info("[LoginAuthenticationSuccessHandler] handleCookie - memberId : {} ", memberId);
        // request에서 아이디 체크 유무 조회
        String rememberMe = request.getParameter("rememberMe");
        log.info("[LoginAuthenticationSuccessHandler] handleCookie - rememberMe : {} ", rememberMe);

        // 아이디 체크가 되어 있음
        if (rememberMe != null) {
            // 쿠키 생성
            Cookie cookie = new Cookie("memberId", memberId);
            log.info("[LoginAuthenticationSuccessHandler] handleCookie - cookie1 : {} ", cookie);
            // 쿠키 도메인 설정
            cookie.setHttpOnly(true); // HTTPS에서만 전송
            cookie.setPath("/"); // 쿠키가 모든 경로에서 유효
            response.setHeader(
                    "Set-Cookie",
                    String.format(
                            "%s=%s; Path=/; HttpOnly; Secure; SameSite=None",
                            cookie.getName(), cookie.getValue()));
            // 1일 간 유지
            cookie.setMaxAge(60 * 60 * 24);
            log.info("[LoginAuthenticationSuccessHandler] handleCookie - cookie2 : {} ", cookie);
            // 쿠키 등록
            response.addCookie(cookie);
            response.setHeader("Access-Control-Allow-Origin", "https://5re5park.site");
            response.setHeader("Access-Control-Allow-Origin", "https://dev1.5re5park.site");
            response.setHeader("Access-Control-Allow-Credentials", "true");

        } else {
            // 아이디 체크가 되어 있지 않음
            Cookie cookie = new Cookie("memberId", "");
            log.info("[LoginAuthenticationSuccessHandler] handleCookie - No cookie : {} ", cookie);
            // 쿠키 수동 삭제
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }
    }
}
