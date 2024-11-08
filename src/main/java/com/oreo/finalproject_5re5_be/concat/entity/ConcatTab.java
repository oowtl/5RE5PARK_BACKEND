package com.oreo.finalproject_5re5_be.concat.entity;

import com.oreo.finalproject_5re5_be.global.entity.BaseEntity;
import com.oreo.finalproject_5re5_be.project.entity.Project;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "concat_tab")
public class ConcatTab extends BaseEntity {
    @Id
    @OneToOne
    @JoinColumn(name = "pro_seq") // 기본 키이면서 외래 키로 사용
    private Project project;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "option_seq")
    private ConcatOption option;

    @Column(name = "status")
    private Character status;

    @Column(name = "front_silence")
    private Float frontSilence;

    @Column(name = "back_silence")
    private Float backSilence;

}


