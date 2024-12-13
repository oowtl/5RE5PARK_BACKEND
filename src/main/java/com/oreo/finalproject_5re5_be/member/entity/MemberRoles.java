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
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "member_roles")
@Getter
@Setter
@ToString
public class MemberRoles extends BaseEntity {

    @Id
    @Column(name = "poli_code")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long poliCode;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "code")
    private Code code;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "detail_cont", nullable = false)
    private String detailCont;

    @Column(name = "appl_reas", nullable = false)
    private String applReas;

    @Column(name = "chk_use", nullable = false)
    private Character chkUse;

    @Column(name = "appl_date", nullable = false)
    private LocalDateTime poliCodeRegDate;
}
