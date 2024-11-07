package com.oreo.finalproject_5re5_be.concat.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(name = "concat_row")
public class ConcatRow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @OneToOne
    @JoinColumn(name = "concat_row_seq")
    private ConcatRow concatRowSeq;

    @OneToOne
    @JoinColumn(name = "pro_seq")
    private Project proSeq;

    private String RowText;
    private Boolean selected;
    private Float silence;
    private int RowIndex;
    private Boolean status;


}
