package com.oreo.finalproject_5re5_be.vc.entity;

import com.oreo.finalproject_5re5_be.global.entity.BaseEntity;
import com.oreo.finalproject_5re5_be.code.entity.Code;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "vc_request_history")
@Getter
@ToString
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = false)
public class VcRequestHistory extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vcrl_seq")
    private Long vcrlSeq;

    @Column(nullable = false, name = "ch_date")
    @CreatedDate
    private LocalDateTime requestDate;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "code")
    private Code ccSeq;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "src_seq")
    private VcSrcFile srcSeq;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "trg_seq")
    private VcTrgFile trgSeq;

    @PrePersist
    public void prePersist() {
        requestDate = LocalDateTime.now();
    }
}
