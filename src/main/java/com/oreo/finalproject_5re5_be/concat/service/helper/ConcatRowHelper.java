package com.oreo.finalproject_5re5_be.concat.service.helper;

import com.oreo.finalproject_5re5_be.concat.entity.ConcatRow;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ConcatRowHelper {
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void batchInsert(List<ConcatRow> rows) {
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
