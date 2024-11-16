package com.oreo.finalproject_5re5_be.vc.repository;


import com.oreo.finalproject_5re5_be.vc.entity.VcTrgFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VcTrgFileRepository extends JpaRepository<VcTrgFile, Long> {
}
