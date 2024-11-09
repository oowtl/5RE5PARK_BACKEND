package com.oreo.finalproject_5re5_be.member.dto.request;

import com.oreo.finalproject_5re5_be.member.entity.MemberTermsHistory;
import com.oreo.finalproject_5re5_be.member.exception.MemberMandatoryTermNotAgreedException;
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

    private String id;
    private String email;
    private String password;
    private String name;
    private String normAddr;
    private String birthDate;
    private String locaAddr;
    private String detailAddr;
    private String passAddr;
    private Character chkValid;
    private LocalDateTime userRegDate;
    private List<MemberTerm> memberTerms;


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

    public MemberTermsHistory createMemberTermsHistoryEntity(Member member) {
        MemberTermsHistory memberTermsHistory = new MemberTermsHistory();

        for (int i=0; i< memberTerms.size(); i++) {
            MemberTerm term = memberTerms.get(i);
            if (!term.isValid()) {
                throw new MemberMandatoryTermNotAgreedException();
            }

            memberTermsHistory.addMemberTermCondition(i+1, term.getAgreed());
        }

        memberTermsHistory.setMember(member);
        return memberTermsHistory;
    }
}
