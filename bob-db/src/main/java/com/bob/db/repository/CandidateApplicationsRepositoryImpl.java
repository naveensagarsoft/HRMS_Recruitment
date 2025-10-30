package com.bob.db.repository;

import com.bob.db.entity.CandidateApplicationsEntity;
import com.bob.db.entity.WorkflowApprovalEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.stereotype.Repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class CandidateApplicationsRepositoryImpl implements CandidateApplicationsRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private WorkflowApprovalEntityRepository workflowApprovalEntityRepository;

    private static final Logger logger = LoggerFactory.getLogger(CandidateApplicationsRepositoryImpl.class);

    @Override
    @Transactional
    public CandidateApplicationsEntity saveWithWorkflow(CandidateApplicationsEntity newEntity) {
        CandidateApplicationsEntity savedEntity = entityManager.merge(newEntity);

        try {
            WorkflowApprovalEntity workflow = WorkflowApprovalEntity.builder()
                    .entityId(savedEntity.getApplicationId())
                    .entityType(CandidateApplicationsEntity.ENTITY_TYPE)
                    .stepNumber(1) // Assuming step 1 for now
                    .approverRole("")
//                    .approverId(null)//TODO: once security utils is migrated to common-util populate this
                    .action(newEntity.getApplicationStatus())
                    .actionDate(LocalDateTime.now())
                    .comments("Status updated to " + newEntity.getApplicationStatus())
                    .status(newEntity.getApplicationStatus())
                    .build();
            workflowApprovalEntityRepository.save(workflow);
        } catch (Exception ex) {
            logger.error("Failed to save workflow approval for applicationId {}: {}", savedEntity.getApplicationId(), ex.getMessage(), ex);
        }

        return savedEntity;
    }

}
