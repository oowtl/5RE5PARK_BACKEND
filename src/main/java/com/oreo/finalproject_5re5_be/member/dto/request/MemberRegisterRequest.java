package com.oreo.finalproject_5re5_be.member.dto.request;

import com.oreo.finalproject_5re5_be.member.entity.MemberTermsHistory;
import com.oreo.finalproject_5re5_be.member.exception.MemberMandatoryTermNotAgreedException;
import com.oreo.finalproject_5re5_be.member.exception.MemberWrongCountTermCondition;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import com.oreo.finalproject_5re5_be.member.entity.Member;

@Getter
@Setter
@Builder
@ToString
public class MemberRegisterRequest {

    @NotBlank(message = "아이디를 입력해주세요.")  // 필수값, 빈 값은 허용하지 않음
    @Pattern(regexp = "^[a-zA-Z0-9]{6,20}$", message = "아이디는 6~20자의 영문 및 숫자만 허용됩니다.")  // 정규식으로 아이디 패턴 검증
    private String id;

    @NotBlank(message = "이메일을 입력해주세요.")  // 필수값, 빈 값은 허용하지 않음
    @Email(message = "유효한 이메일 형식이 아닙니다.")  // 이메일 형식 검증
    private String email;

    @NotBlank(message = "비밀번호를 입력해주세요.")  // 필수값, 빈 값은 허용하지 않음
    @Pattern(regexp = "^(?!.*(.)\\1{3})(?=.*[!@#$%^&*()_+=-])[A-Za-z\\d!@#$%^&*()_+=-]{8,20}$", message = "비밀번호는 8~29자의 특수문자를 포함해야하며, 동일 문자 4회 이상 연속 불가합니다.")  // 비밀번호 패턴 검증
    private String password;

    @NotBlank(message = "이름을 입력해주세요.")  // 필수값, 빈 값은 허용하지 않음
    @Pattern(regexp = "^[a-zA-Z가-힣]{1,20}$", message = "이름은 특수문자, 숫자를 포함할 수 없습니다.")  // 이름 패턴 검증
    private String name;

    @NotBlank(message = "주소를 입력해주세요.")  // 필수값, 빈 값은 허용하지 않음
    private String normAddr;

    @NotBlank(message = "생년월일을 입력해주세요.")  // 필수값, 빈 값은 허용하지 않음
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "생년월일은 'YYYY-MM-DD' 형식으로 입력해주세요.")  // 생년월일 형식 검증
    private String birthDate;

    private String locaAddr;
    private String detailAddr;
    private String passAddr;

    @NotNull(message = "필수 동의 항목이 선택되지 않았습니다.")  // 필수값 검증
    private Character chkValid;

    private LocalDateTime userRegDate;

    @NotEmpty(message = "필수 약관 동의 항목이 비어 있습니다.")  // 약관 요청 리스트가 비어 있으면 오류
    private List<@Valid MemberTermRequest> memberTermRequests;  // 리스트의 각 항목에 대해 검증

    // Member 엔티티로 변환
    public Member createMemberEntity() {
        return Member.builder()
                .id(id)
                .email(email)
                .password(password)
                .name(name)
                .normAddr(normAddr)
                .birthDate(birthDate)
                .locaAddr(locaAddr)
                .detailAddr(detailAddr)
                .passAddr(passAddr)
                .chkValid(chkValid)
                .userRegDate(userRegDate)
                .build();
    }

    // 약관 동의 항목 검증
    public void checkValidTerms() {
        for (MemberTermRequest term : memberTermRequests) {
            if (!term.isValid()) {
                throw new MemberMandatoryTermNotAgreedException();
            }
        }
    }

    // 약관 동의 항목 개수 검증
    public void checkValidTermsCount() {
        if (!(memberTermRequests.size() == 5)) {
            throw new MemberWrongCountTermCondition();
        }
    }

    // 회원 등록 dto로부터 약관 이력 엔티티 생성
    public MemberTermsHistory createMemberTermsHistoryEntity(Member member) {
        MemberTermsHistory memberTermsHistory = new MemberTermsHistory();

        for (int i = 0; i < memberTermRequests.size(); i++) {
            MemberTermRequest term = memberTermRequests.get(i);
            memberTermsHistory.addMemberTermCondition(i + 1, term.getAgreed());
        }

        memberTermsHistory.setMember(member);
        return memberTermsHistory;
    }
}