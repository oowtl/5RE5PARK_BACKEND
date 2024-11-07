package com.oreo.finalproject_5re5_be.user.entity;


import com.oreo.finalproject_5re5_be.utils.entity.BaseEntity;
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
@Table(name = "user_roles_category")
@Getter @Setter
@ToString
public class UserRolesCategory extends BaseEntity {

    @Id
    @Column(name = "cate_code")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long cateCode;

    private String name;
    private String comt;
}
