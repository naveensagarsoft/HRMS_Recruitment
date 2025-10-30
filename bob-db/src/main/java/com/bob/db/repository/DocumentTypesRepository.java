package com.bob.db.repository;

import com.bob.db.entity.DocumentTypesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentTypesRepository extends JpaRepository<DocumentTypesEntity, Long> {
    List<DocumentTypesEntity> findAllByIsActiveTrue();
}
