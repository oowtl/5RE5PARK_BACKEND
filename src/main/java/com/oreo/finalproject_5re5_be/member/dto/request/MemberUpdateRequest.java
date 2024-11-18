package com.oreo.finalproject_5re5_be.member.dto.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
public class MemberUpdateRequest {

    @NotBlank(message = "아이디를 입력해주세요.")  // 필수값, 빈 값은 허용하지 않음
    @Pattern(regexp = "^[a-zA-Z0-9]{6,20}$", message = "아이디는 6~20자의 영문 및 숫자만 허용됩니다.")  // 정규식으로 아이디 패턴 검증
    private String id;

    @NotBlank(message = "이메일을 입력해주세요.")  // 필수값, 빈 값은 허용하지 않음
    @Email(message = "유효한 이메일 형식이 아닙니다.")  // 이메일 형식 검증
    private String email;

    @NotBlank(message = "비밀번호를 입력해주세요.")  // 필수값, 빈 값은 허용하지 않음
    @Pattern(regexp = "^(?!.*(.)\\1{3})(?=.*[!@#$%^&*()_+=-])[A-Za-z\\d!@#$%^&*()_+=-]{8,20}$", message = "비밀번호는 8~20자의 특수문자를 포함해야하며, 동일 문자 4회 이상 연속 불가합니다.")  // 비밀번호 패턴 검증
    private String password;

    @NotBlank(message = "이름을 입력해주세요.")  // 필수값, 빈 값은 허용하지 않음
    @Pattern(regexp = "^[a-zA-Z가-힣]{1,20}$", message = "이름은 특수문자, 숫자를 포함할 수 없습니다.")  // 이름 패턴 검증
    private String name;

    @NotBlank(message = "주소를 입력해주세요.")  // 필수값, 빈 값은 허용하지 않음
    private String normAddr;


}
