package com.oreo.finalproject_5re5_be.tts.entity;

import com.oreo.finalproject_5re5_be.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "voice")
@Getter
@ToString
// builder 패턴을 사용할 수 있게 해주는 어노테이션
@Builder(toBuilder = true) // toBuilder = true : 객체의 일부 값을 변경하여 생성시키고 싶을 때 사용한다.
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 파라미터가 없는 생성자를 만들어주는 어노테이션, PROTECTED 접근 제어자
@AllArgsConstructor(access = AccessLevel.PRIVATE) // 전체 파라미터를 가지는 생성자를 만들어주는 어노테이션, PRIVATE 접근 제어자
// equals()와 hashCode() 메소드를 자동으로 생성해주는 어노테이션
@EqualsAndHashCode(callSuper = false) // callSuper = false : 부모 클래스의 필드를 비교하지 않는다.
public class Voice extends BaseEntity {
    @Id
    @Column(name = "voice_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long voiceSeq;

    @Column(name = "name")
    private String name;

    @Column(name = "gender")
    private String gender;

    @Column(name = "age")
    private Integer age;

    @Column(name = "dscp")
    private String description;

    @Column(name = "enabled")
    private char enabled;

    @Column(name = "server", nullable = false)
    private String server;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "lang_seq")
    private Language language;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "style_seq")
    private Style style;
}
