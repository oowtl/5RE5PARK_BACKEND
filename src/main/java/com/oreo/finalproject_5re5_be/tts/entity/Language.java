package com.oreo.finalproject_5re5_be.tts.entity;

import com.oreo.finalproject_5re5_be.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "language")
@Getter
@ToString
// builder 패턴을 사용할 수 있게 해주는 어노테이션
@Builder(toBuilder = true) // toBuilder = true : 객체의 일부 값을 변경하여 생성시키고 싶을 때 사용한다.
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 파라미터가 없는 생성자를 만들어주는 어노테이션, PROTECTED 접근 제어자
@AllArgsConstructor(access = AccessLevel.PRIVATE) // 전체 파라미터를 가지는 생성자를 만들어주는 어노테이션, PRIVATE 접근 제어자
// equals()와 hashCode() 메소드를 자동으로 생성해주는 어노테이션
@EqualsAndHashCode(callSuper = false) // callSuper = false : 부모 클래스의 필드를 비교하지 않는다.
public class Language extends BaseEntity {
    @Id
    @Column(name = "lang_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long langSeq;

    @Column(name = "lang_code", nullable = false)
    private String langCode;

    @Column(name = "lang_name", nullable = false)
    private String langName;

    @Column(name = "regn_code", nullable = false)
    private String regionCode;

    @Column(name = "regn_name", nullable = false)
    private String regionName;

    @Column(name = "enabled", nullable = false)
    private char enabled;
}
