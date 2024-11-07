package com.oreo.finalproject_5re5_be.global.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {

    @CreatedDate
    @Column(updatable = false, name = "reg_date")
    private LocalDateTime regDate;

    @LastModifiedDate
    @Column(insertable = false, name = "up_date")
    private LocalDateTime upDate;

    @CreatedBy
    @Column(updatable = false, name ="reg_seq")
    private Long regSeq;

    @LastModifiedBy
    @Column(insertable = false, name = "up_seq")
    private Long upSeq;
}