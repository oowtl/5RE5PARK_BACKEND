package com.oreo.finalproject_5re5_be.member.entity;

import com.oreo.finalproject_5re5_be.global.entity.BaseEntity;
import com.oreo.finalproject_5re5_be.member.dto.request.MemberRemoveRequest;
import com.oreo.finalproject_5re5_be.member.repository.MemberDeleteRepository;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Entity
@Table(name = "member_delete")
@Getter @Setter
@ToString
@EqualsAndHashCode(callSuper = false)
public class MemberDelete extends BaseEntity {

    @Id
    @Column(name = "del_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long delSeq;

    private Long memberSeq; // 기존의 회원 시퀀스

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "code")
    private Code code; // 회원 삭제 유형 코드

    @Column(name = "detail_cont", nullable = false)
    private String detailCont; // 회원 삭제 사유

    @Column(name = "chk_use")
    private Character chkUse; // 적용 유무

    @Column(name = "appl_date", nullable = false)
    private String applDate;

    public static MemberDelete of(Long memberSeq, MemberRemoveRequest request, Code code) {
        MemberDelete memberDelete = new MemberDelete();

        // 현재 시간 조회
        LocalDateTime now = LocalDateTime.now();

        // DATETIME 형식으로 변환하기 위한 포맷터 생성
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // 포맷팅된 문자열로 변환
        String formattedNow = now.format(formatter);

        memberDelete.setMemberSeq(memberSeq);
        memberDelete.setCode(code);
        memberDelete.setChkUse('N');
        memberDelete.setApplDate(formattedNow);
        memberDelete.setDetailCont(request.getDetailCont());

        return memberDelete;
    }
}
