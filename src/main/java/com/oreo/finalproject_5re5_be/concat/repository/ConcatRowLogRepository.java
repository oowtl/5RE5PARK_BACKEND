package com.oreo.finalproject_5re5_be.concat.repository;

import com.oreo.finalproject_5re5_be.concat.entity.ConcatRowLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConcatRowLogRepository extends JpaRepository<ConcatRowLog, Long> {}
