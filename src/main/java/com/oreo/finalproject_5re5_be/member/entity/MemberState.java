package com.oreo.finalproject_5re5_be.member.entity;

import com.oreo.finalproject_5re5_be.code.entity.Code;
import com.oreo.finalproject_5re5_be.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "member_state")
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = false)
public class MemberState extends BaseEntity {

    @Id
    @Column(name = "state_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long stateSeq;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "member_seq")
    private Member member;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "code")
    private Code code;

    @Column(name = "appl_date", nullable = false)
    private String applDate;

    @Column(name = "end_date", nullable = false)
    private String endDate;

    public static MemberState of(Member member, Code code) {
        MemberState memberState = new MemberState();

        // 현재 시간과 최대 시간 세팅
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.MAX;

        // DATETIME 형식으로 변환하기 위한 포맷터 생성
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // 포맷팅된 문자열로 변환
        String formattedDateTime = now.format(formatter);
        String formattedEnd = end.format(formatter);

        // 회원 상태에 생성된 회원과 신규 등록 회원 카테고리 그리고 시간 세팅
        memberState.setMember(member);
        memberState.setCode(code);
        memberState.setApplDate(formattedDateTime);
        memberState.setEndDate(formattedEnd);

        return memberState;
    }
}
