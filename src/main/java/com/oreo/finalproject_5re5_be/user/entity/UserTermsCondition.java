package com.oreo.finalproject_5re5_be.user.entity;

import com.oreo.finalproject_5re5_be.utils.entity.BaseEntity;
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
@Table(name = "user_terms_condition")
@Getter @Setter
@ToString
public class UserTermsCondition extends BaseEntity  {

    @Id
    @Column(name = "terms_cond_code")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long termCondCode;
    private String name;
    private String shortCont;
    private String longCont;
    private Character chkUse;
    private LocalDateTime termCondDate;
    private LocalDateTime termCondUpDate;
    private String law1;
    private String law2;
    private String law3;
}
