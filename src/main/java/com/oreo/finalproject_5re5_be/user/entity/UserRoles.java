package com.oreo.finalproject_5re5_be.user.entity;

import com.oreo.finalproject_5re5_be.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Entity
@Table(name = "user_roles")
@Getter @Setter
@ToString
public class UserRoles extends BaseEntity {

    @Id
    @Column(name = "poli_code")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long poliCode;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cate_code")
    private UserCategory cateCode;

    private String name;
    private String detailCont;
    private String applReas;
    private Character chkUse;
    private LocalDateTime poliCodeRegDate;

}
