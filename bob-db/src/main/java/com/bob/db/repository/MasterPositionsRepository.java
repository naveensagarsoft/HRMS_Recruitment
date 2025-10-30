package com.bob.db.repository;

import com.bob.db.entity.MasterPositionsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MasterPositionsRepository extends JpaRepository<MasterPositionsEntity, Long> {
    // Additional query methods if needed

    @Query("SELECT m.positionCode FROM MasterPositionsEntity m ORDER BY m.masterPositionId DESC LIMIT 1")
    String findLatestPositionCode();

    List<MasterPositionsEntity> findByIsActiveTrue();

//    List<MasterPositionsEntity> findByIsActiveTrueOrIsActiveIsNull();
}
