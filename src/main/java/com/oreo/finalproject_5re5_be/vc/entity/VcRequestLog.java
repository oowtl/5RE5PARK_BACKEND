package com.oreo.finalproject_5re5_be.vc.entity;

import com.oreo.finalproject_5re5_be.project.entity.Project;
import com.oreo.finalproject_5re5_be.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "vc_requestlog")
@Getter
@Setter
@ToString
public class VcRequestLog extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vcrl_seq")
    private Long vcrlSeq;

    @Column(nullable = false, name = "ch_date")
    @CreatedDate
    private LocalDateTime RequestDate;
    @Column(nullable = false, name = "suc_date")
    private LocalDateTime SuccessDate;

    @Column(nullable = false, name = "cc_seq")
    private Long ccSeq;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "src_seq")
    private VcSrcFile srcSeq;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vc_seq")
    private Vc vcSeq;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "pro_seq")
    private Project proSeq;

}
