package com.bob.db.repository;

import com.bob.db.entity.CandidatesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CandidateRepository extends JpaRepository<CandidatesEntity, UUID> {


    List<CandidatesEntity> findByCandidateIdInAndIsActiveTrue(List<UUID> candidateIds);
}
