package com.oreo.finalproject_5re5_be.concat.service.helper;

import com.oreo.finalproject_5re5_be.concat.entity.AudioFile;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class AudioFileHelper {
    @PersistenceContext private EntityManager entityManager;

    @Transactional
    public void batchInsert(List<AudioFile> rows) {
        for (int i = 0; i < rows.size(); i++) {
            entityManager.persist(rows.get(i));
            if (i > 0 && i % 20 == 0) { // 20개마다 flush & clear
                entityManager.flush();
                entityManager.clear();
            }
        }
        entityManager.flush();
        entityManager.clear();
    }
}
