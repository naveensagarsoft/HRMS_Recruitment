package com.bob.db.repository;

import com.bob.db.entity.ReservationCategoriesEntity;
import com.bob.db.entity.SpecialCategoriesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationCategoriesRepository extends JpaRepository<ReservationCategoriesEntity,Long> {
    List<ReservationCategoriesEntity> findAllByIsActiveTrue();
}
