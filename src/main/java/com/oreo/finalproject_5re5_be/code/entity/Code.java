package com.oreo.finalproject_5re5_be.code.entity;

import com.oreo.finalproject_5re5_be.global.entity.BaseEntity;
import com.oreo.finalproject_5re5_be.code.dto.request.CodeUpdateRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "code")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = false)
@Builder
public class Code extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "code_seq", nullable = false)
    private Long codeSeq;

    @Column(name = "cate_num", nullable = false, length = 30)
    private String cateNum;

    @Column(name = "code", nullable = false, length = 30)
    private String code;

    @Column(name = "name", length = 30)
    private String name;

    @Column(name = "ord")
    private Integer ord;

    @Column(name = "chk_use", length = 1, columnDefinition = "char(1) default 'Y'")
    private String chkUse;

    @Column(name = "comt", length = 250)
    private String comt;

    public void update(CodeUpdateRequest request) {
        this.name = request.getName();
        this.ord = request.getOrd();
        this.chkUse = request.getChkUse();
        this.comt = request.getComt();
    }

}

