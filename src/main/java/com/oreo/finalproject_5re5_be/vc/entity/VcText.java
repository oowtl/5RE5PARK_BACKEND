package com.oreo.finalproject_5re5_be.vc.entity;

import com.oreo.finalproject_5re5_be.global.entity.BaseEntity;
import com.oreo.finalproject_5re5_be.project.entity.Project;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "vc_text")
@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class VcText extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vt_seq")
    private Long vtSeq;

    @Column(nullable = false, name = "comment")
    private String comment;
    @Column(nullable = false, name = "length")
    private String length;
    @Column(nullable = false, name = "vt_up_date")
    @LastModifiedDate
    private LocalDateTime vtUpDate;
    @Column(nullable = false, name = "vt_date", updatable = false)
    @CreatedDate
    private LocalDateTime vtDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vc_seq")
    private Vc vcSeq;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "pro_seq")
    private Project proSeq;
}
