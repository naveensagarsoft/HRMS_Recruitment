package com.bob.db.repository;

import com.bob.db.entity.PositionsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Repository
public interface PositionsRepository extends JpaRepository<PositionsEntity, UUID> {
    List<PositionsEntity> findByPositionIdIn(List<UUID> positionIds);

    List<PositionsEntity> findByRequisitionIdIn(List<UUID> reqIds);

    List<PositionsEntity> findAllByRequisitionId(UUID requisitionId);

    List<PositionsEntity> findAllByIsActiveTrue();

//    List<PositionsEntity> findAllByPositionIdInAndIsActiveTrue(Collection<UUID> positionIds);

    List<PositionsEntity> findAllByRequisitionIdInAndPositionStatus(Collection<UUID> requisitionIds, String positionStatus);


    List<PositionsEntity> findByRequisitionIdAndIsActive(UUID requisitionId, Boolean isActive);

    List<PositionsEntity> findByPositionIdInAndIsActive(Collection<UUID> positionIds, Boolean isActive);

    List<PositionsEntity> findByJobRelaxationPolicyId(UUID jobRelaxationPolicyId);
}
