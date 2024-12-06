package com.oreo.finalproject_5re5_be.concat.entity;

import com.oreo.finalproject_5re5_be.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@ToString
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Entity(name = "concat_row")
public class ConcatRow extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "concat_row_seq_generator")
    @SequenceGenerator(
            name = "concat_row_seq_generator",
            sequenceName = "concat_row_seq", // 실제 시퀀스 이름
            allocationSize = 1              // ID를 하나씩 할당
    )
    @Column(name = "concat_row_seq")
    private Long concatRowSeq;


    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "pro_seq")
    private ConcatTab concatTab;

    @Column(name = "row_text")
    private String rowText;
    @Column(name = "selected")
    private Character selected;
    @Column(name = "silence")
    private Float silence;
    @Column(name = "row_index")
    private Integer rowIndex;
    @Column(name = "status")
    private Character status;


}
