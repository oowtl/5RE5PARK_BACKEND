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
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.modelmapper.internal.bytebuddy.asm.Advice.Local;


@Entity
@Table(name = "user_connection_history")
@Getter @Setter
@ToString
public class UserConnectionHistory extends BaseEntity {

    @Id
    @Column(name = "conn_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long connSeq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq")
    private User user;

    private LocalDateTime connDate;

}
