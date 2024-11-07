package com.oreo.finalproject_5re5_be.user.entity;


import com.oreo.finalproject_5re5_be.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "user_category")
@Getter @Setter
@ToString
public class UserCategory extends BaseEntity {

    @Id
    @Column(name = "cate_code")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cateCode;

    private String name;
    private String comt;
}
