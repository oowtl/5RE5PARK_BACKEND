package com.oreo.finalproject_5re5_be.user.entity;

import com.oreo.finalproject_5re5_be.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "user")
@Getter @Setter
@ToString
public class User extends BaseEntity {

    @Id
    @Column(name = "user_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userSeq;
    private String id;
    private String email;
    private String name;
    private String phon;
    private LocalDateTime userRegDate;
    private Character chkValid;
    private String normAddr;
    private String passAddr;
    private String locaAddr;
    private String detailAddr;
}
