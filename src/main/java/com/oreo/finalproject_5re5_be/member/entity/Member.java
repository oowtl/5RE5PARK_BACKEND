package com.oreo.finalproject_5re5_be.member.entity;

import com.oreo.finalproject_5re5_be.global.entity.BaseEntity;
import com.oreo.finalproject_5re5_be.member.dto.request.MemberUpdateRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "member")
@Getter @Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Member extends BaseEntity {

    @Id
    @Column(name = "member_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    @Column(name = "id", nullable = false)
    private String id;
    @Column(name = "email", nullable = false)
    private String email;
    @Column(name = "pwd", nullable = false)
    private String password;
    @Column(name = "birth_date")
    private String birthDate;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "phon")
    private String phon;
    @Column(name = "member_reg_date")
    private LocalDateTime memberRegDate;
    @Column(name = "chk_valid", nullable = false)
    private Character chkValid;
    @Column(name = "norm_addr")
    private String normAddr;
    @Column(name = "pass_addr")
    private String passAddr;
    @Column(name = "loca_addr")
    private String locaAddr;
    @Column(name = "detail_addr")
    private String detailAddr;


    public void update(MemberUpdateRequest request) {
        this.id = request.getId();
        this.email = request.getEmail();
        this.password = request.getPassword();
        this.name = request.getName();
        this.normAddr = request.getNormAddr();
    }
}
