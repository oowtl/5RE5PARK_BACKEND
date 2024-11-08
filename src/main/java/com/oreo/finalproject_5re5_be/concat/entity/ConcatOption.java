package com.oreo.finalproject_5re5_be.concat.entity;


import com.oreo.finalproject_5re5_be.utils.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(name = "concat_option")
public class ConcatOption extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long optionSeq;

    @ManyToOne
    @JoinColumn(name = "audio_file_seq")
    private AudioFile audioFile;


    private String optionName;
    private String optionComment;

}
