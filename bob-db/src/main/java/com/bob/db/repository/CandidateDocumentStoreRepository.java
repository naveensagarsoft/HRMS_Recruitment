package com.bob.db.repository;

import com.bob.db.entity.CandidateDocumentStoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CandidateDocumentStoreRepository extends JpaRepository<CandidateDocumentStoreEntity, UUID> {
    Optional<CandidateDocumentStoreEntity> findByCandidateIdAndDocumentId(UUID candidateId, Long documentId);

    List<CandidateDocumentStoreEntity> findAllByCandidateId(UUID candidateId);

    Optional<CandidateDocumentStoreEntity> findByCandidateIdAndDocumentIdAndFileName(UUID candidateId, Long documentId, String fileName);
}
