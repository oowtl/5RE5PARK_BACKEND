package com.oreo.finalproject_5re5_be.user.entity;

import com.oreo.finalproject_5re5_be.utils.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "user_state")
@Getter @Setter
@ToString
public class UserState extends BaseEntity {

    @Id
    @Column(name = "state_code")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long StateSeq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq")
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "state_code")
    private UserCategory stateCode;

    private LocalDateTime applDate;
    private LocalDateTime endDate;
}
