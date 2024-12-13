package com.oreo.finalproject_5re5_be.tts.entity;

import com.oreo.finalproject_5re5_be.global.entity.BaseEntity;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

@Entity
@Table(name = "sample_audio")
@Getter
@ToString
// builder 패턴을 사용할 수 있게 해주는 어노테이션
@Builder(toBuilder = true) // toBuilder = true : 객체의 일부 값을 변경하여 생성시키고 싶을 때 사용한다.
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 파라미터가 없는 생성자를 만들어주는 어노테이션, PROTECTED 접근 제어자
@AllArgsConstructor(access = AccessLevel.PRIVATE) // 전체 파라미터를 가지는 생성자를 만들어주는 어노테이션, PRIVATE 접근 제어자
// equals()와 hashCode() 메소드를 자동으로 생성해주는 어노테이션
@EqualsAndHashCode(
        callSuper = false,
        exclude = {"createdAt"}) // callSuper = false : 부모 클래스의 필드를 비교하지 않는다.
public class SampleAudio extends BaseEntity {
    @Id
    @Column(name = "smpl_aud_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sampleAudioSeq;

    @Column(name = "aud_path", nullable = false)
    private String audioPath;

    @Column(name = "aud_name")
    private String audioName;

    @Column(name = "aud_ext")
    private String audioExtension;

    @Column(name = "aud_size")
    private String audioSize;

    @Column(name = "aud_time")
    private String audioTime;

    @Column(name = "script")
    private String script;

    @Column(name = "enabled", nullable = false)
    private char enabled;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "voice_seq")
    private Voice voice;
}
