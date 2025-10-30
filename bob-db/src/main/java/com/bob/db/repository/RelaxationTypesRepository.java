package com.bob.db.repository;

import com.bob.db.entity.RelaxationTypesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RelaxationTypesRepository extends JpaRepository<RelaxationTypesEntity, Integer> {

    List<RelaxationTypesEntity> findAllByIsActiveTrue();
}
