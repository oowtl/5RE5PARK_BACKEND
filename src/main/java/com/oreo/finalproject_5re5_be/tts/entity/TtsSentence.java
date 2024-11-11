package com.oreo.finalproject_5re5_be.tts.entity;

import com.oreo.finalproject_5re5_be.global.entity.BaseEntity;
import com.oreo.finalproject_5re5_be.project.entity.Project;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Entity
@Table(name = "tts_sentence")
@Getter
@ToString
// builder 패턴을 사용할 수 있게 해주는 어노테이션
@Builder(toBuilder = true) // toBuilder = true : 객체의 일부 값을 변경하여 생성시키고 싶을 때 사용한다.
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 파라미터가 없는 생성자를 만들어주는 어노테이션, PROTECTED 접근 제어자
@AllArgsConstructor(access = AccessLevel.PRIVATE) // 전체 파라미터를 가지는 생성자를 만들어주는 어노테이션, PRIVATE 접근 제어자
// equals()와 hashCode() 메소드를 자동으로 생성해주는 어노테이션
@EqualsAndHashCode(callSuper = false) // callSuper = false : 부모 클래스의 필드를 비교하지 않는다.
public class TtsSentence extends BaseEntity {
    @Id
    @Column(name = "ts_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tsSeq;

    @Column(name = "text", nullable = false)
    private String text;

    @Column(name = "sort_ord", nullable = false)
    private Integer sortOrder;

    @Column(name = "volume")
    private Integer volume;

    @Column(name = "speed")
    private Float speed;

    @Column(name = "st_pitch")
    private Integer startPitch;

    @Column(name = "emotion")
    private Integer emotion;

    @Column(name = "emt_strength")
    private Integer emotionStrength;

    @Column(name = "smpl_rate")
    private Integer sampleRate;

    @Column(name = "alpha")
    private Integer alpha;

    @Column(name = "end_pitch")
    private Float endPitch;

    @Column(name = "aud_fmt")
    private String audioFormat;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "pro_seq", nullable = false)
    private Project project;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tts_aud_seq")
    private TtsAudioFile ttsAudiofile;

    @OneToOne(optional = false)
    @JoinColumn(name = "voice_seq", nullable = false)
    private Voice voice;
}
