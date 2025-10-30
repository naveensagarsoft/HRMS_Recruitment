package com.bob.db.repository;

import com.bob.db.entity.CandidatesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CandidatesRepository extends JpaRepository<CandidatesEntity, UUID> {
    List<CandidatesEntity> findByCandidateIdIn(List<UUID> candidateIds);

    @Query("""
        SELECT c FROM CandidatesEntity c WHERE c.isBulkUpload = true AND c.candidateId NOT IN (
            SELECT ca.candidateId FROM CandidateApplicationsEntity ca WHERE ca.positionId = :positionId
        ) AND c.createdBy = :createdBy
    """)
    List<CandidatesEntity> findBulkUploadNotAppliedForPosition(@Param("positionId") java.util.UUID positionId, @Param("createdBy") String createdBy);
}
