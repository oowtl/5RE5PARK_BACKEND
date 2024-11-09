package com.oreo.finalproject_5re5_be.member.entity;

import com.oreo.finalproject_5re5_be.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
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
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Member extends BaseEntity {

    @Id
    @Column(name = "member_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    private String id;
    private String email;
    private String password;
    private String birthDate;
    private String name;
    private String phon;
    private LocalDateTime userRegDate;
    private Character chkValid;
    private String normAddr;
    private String passAddr;
    private String locaAddr;
    private String detailAddr;




}
