package com.oreo.finalproject_5re5_be.member.entity;


import com.oreo.finalproject_5re5_be.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "member_category")
@Getter @Setter
@ToString
public class MemberCategory extends BaseEntity {

    @Id
    @Column(name = "cate_code")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cateCode;
    @Column(name = "name")
    private String name;
    @Column(name = "comt")
    private String comt;
}
