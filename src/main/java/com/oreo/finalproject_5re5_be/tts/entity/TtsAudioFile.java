package com.oreo.finalproject_5re5_be.tts.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.oreo.finalproject_5re5_be.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "tts_audio_file")
@Getter
@ToString
// builder 패턴을 사용할 수 있게 해주는 어노테이션
@Builder(toBuilder = true) // toBuilder = true : 객체의 일부 값을 변경하여 생성시키고 싶을 때 사용한다.
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 파라미터가 없는 생성자를 만들어주는 어노테이션, PROTECTED 접근 제어자
@AllArgsConstructor(access = AccessLevel.PRIVATE) // 전체 파라미터를 가지는 생성자를 만들어주는 어노테이션, PRIVATE 접근 제어자
// equals()와 hashCode() 메소드를 자동으로 생성해주는 어노테이션
@EqualsAndHashCode(callSuper = false, exclude = {"createdAt"}) // callSuper = false : 부모 클래스의 필드를 비교하지 않는다.
public class TtsAudioFile extends BaseEntity {
    @Id
    @Column(name = "tts_aud_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ttsAudioSeq;

    @Column(name = "aud_name", nullable = false)
    private String audioName;

    @Column(name = "aud_path", nullable = false)
    private String audioPath;

    @Column(name = "aud_ext")
    private String audioExtension;

    @Column(name = "aud_size")
    private String audioSize;

    @Column(name = "aud_time")
    private Integer audioTime;

    @Column(name = "down_cnt")
    @ColumnDefault("1")
    private Integer downloadCount;

    @Column(name = "down_yn")
    private char downloadYn;

    @Column(name = "aud_play_yn")
    private char audioPlayYn;

    @CreatedDate
    @Column(updatable = false, name = "created_at")
    private LocalDateTime createdAt;
}
