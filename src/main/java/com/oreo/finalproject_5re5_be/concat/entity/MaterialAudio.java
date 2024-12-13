package com.oreo.finalproject_5re5_be.concat.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.oreo.finalproject_5re5_be.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@ToString
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "material_audio")
public class MaterialAudio extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "concat_result_seq")
    @ToString.Exclude // 순환 참조 방지
    @JsonIgnore // JSON 직렬화에서 제외
    private ConcatResult concatResult;

    @ManyToOne
    @JoinColumn(name = "audio_file_seq")
    @ToString.Exclude // 순환 참조 방지
    @JsonIgnore // JSON 직렬화에서 제외
    private AudioFile audioFile;

    @Column(name = "method")
    private String method;
}
