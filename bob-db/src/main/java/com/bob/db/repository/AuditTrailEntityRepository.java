package com.bob.db.repository;

import com.bob.db.entity.AuditTrailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AuditTrailEntityRepository extends JpaRepository<AuditTrailEntity, UUID> {
    // Additional custom queries can be added here if needed
}

