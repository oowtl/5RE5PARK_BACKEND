package com.oreo.finalproject_5re5_be.tts.entity;

import com.oreo.finalproject_5re5_be.global.entity.BaseEntity;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "tts_progress_status")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Builder
@ToString
@EqualsAndHashCode(callSuper = false)
public class TtsProgressStatus extends BaseEntity {
    @Id
    @Column(name = "tps_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tpsSeq;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "ts_seq", nullable = false)
    private TtsSentence ttsSentence;

    @Enumerated(EnumType.STRING)
    @Column(name = "prog_stat", nullable = false)
    private TtsProgressStatusCode progressStatus;

    @CreationTimestamp
    @Column(name = "chg_date", nullable = false)
    private LocalDateTime changed_at;
}
