package com.oreo.finalproject_5re5_be.concat.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(name = "concat_row_log")
public class ConcatRowLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long concatLogSeq;

    private Long proSeq;
    private Long concatRowSeq;

    private Long modifiedNum;
    private LocalDateTime modifiedDate;
    private String RequestContext;
    private Boolean selected;
    private Float silence;

}
