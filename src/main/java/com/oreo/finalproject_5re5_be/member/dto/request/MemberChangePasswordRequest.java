package com.oreo.finalproject_5re5_be.member.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class MemberChangePasswordRequest {

    @NotBlank(message = "비밀번호를 입력해주세요.") // 필수값, 빈 값은 허용하지 않음
    @Pattern(
            regexp = "^(?!.*(.)\\1{3})(?=.*[!@#$%^&*()_+=-])[A-Za-z\\d!@#$%^&*()_+=-]{8,20}$",
            message = "비밀번호는 8~29자의 특수문자를 포함해야하며, 동일 문자 4회 이상 연속 불가합니다.") // 비밀번호 패턴 검증
    private String password;
}
