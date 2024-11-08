package com.oreo.finalproject_5re5_be.tts.entity;

import com.oreo.finalproject_5re5_be.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "tts_progress_status")
@Getter
@Setter
public class TtsProgressStatus extends BaseEntity {
    @Id
    @Column(name = "tps_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tsSeq;

    @UpdateTimestamp
    @Column(name = "chg_date", nullable = false)
    private LocalDateTime changed_at;

}
