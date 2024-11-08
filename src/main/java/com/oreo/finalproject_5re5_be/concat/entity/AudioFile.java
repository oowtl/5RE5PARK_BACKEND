package com.oreo.finalproject_5re5_be.concat.entity;


import com.oreo.finalproject_5re5_be.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(name = "audio_file")
public class AudioFile extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "audio_file_seq")
    private Long audioFileSeq;

    @OneToOne
    @Column(name = "concat_row_Seq")
    private ConcatRow concatRowSeq;

    private String audioUrl;
    private String extension;
    private Long fileSize;
    private Long fileLength;
    private String fileName;
    private LocalDateTime createdDate;


}
