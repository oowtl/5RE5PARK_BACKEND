package com.oreo.finalproject_5re5_be.vc.entity;

import com.oreo.finalproject_5re5_be.global.entity.BaseEntity;
import com.oreo.finalproject_5re5_be.project.entity.Project;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "vc_resultlog")
@Getter
@ToString
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = false)
public class VcResultLog extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vrl_seq")
    private Long vrlSeq;

    @Column(nullable = false, name = "vrl_reg_date")
    @CreatedDate
    private LocalDateTime date;

    @Column(nullable = false, name = "cc_seq")
    private Long ccSeq;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "pro_seq")
    private Project proSeq;
}
