package com.bob.db.repository;

import com.bob.db.entity.CandidateDocumentsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
@Repository
public interface CandidateDocumentsRepository extends JpaRepository<CandidateDocumentsEntity, UUID> {

    //    @Query(value = "SELECT * FROM candidate_documents WHERE candidate_id = :candidateId AND application_id = :applicationId", nativeQuery = true)
    List<CandidateDocumentsEntity> findByCandidateIdAndApplicationId(@Param("candidateId") UUID candidateId, @Param("applicationId") UUID applicationId);




}
