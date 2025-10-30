package com.bob.db.repository;

import com.bob.db.entity.CandidateApplicationsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
@Repository
public interface CandidateApplicationsRepository extends JpaRepository<CandidateApplicationsEntity, UUID>,CandidateApplicationsRepositoryCustom {

    List<CandidateApplicationsEntity> findByPositionId(UUID positionId);

    List<CandidateApplicationsEntity> findByCandidateId(UUID candidateId);

    List<CandidateApplicationsEntity> findByCandidateIdAndPositionId(UUID candidateId, UUID positionId);

    List<CandidateApplicationsEntity> findByApplicationStatus(String status);




}
