package com.oreo.finalproject_5re5_be.global.config;


import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

@RequiredArgsConstructor
public class AuditorAwareImpl implements AuditorAware<Long> {
    //session 값
    private final HttpSession session;
    //session 값으로 memberSeq 가지고 오기
    @Override
    public Optional<Long> getCurrentAuditor() {
        Long memberID = (Long) session.getAttribute("memberSeq");
        return Optional.ofNullable(memberID);
    }
}
