package com.oreo.finalproject_5re5_be.concat.entity;

import com.oreo.finalproject_5re5_be.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@ToString
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "concat_row_log")
public class ConcatRowLog extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "concat_log_seq")
    private Long concatRowLogSeq;

    @ManyToOne
    @JoinColumn(name = "concat_row_seq")
    private ConcatRow concatRow;

    private Long modifiedNum;
    private LocalDateTime modifiedDate;
    private String RequestContext;
    private Character selected;
    private Float silence;

}
