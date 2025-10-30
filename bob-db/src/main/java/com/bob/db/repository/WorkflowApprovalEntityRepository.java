package com.bob.db.repository;

import com.bob.db.entity.WorkflowApprovalEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface WorkflowApprovalEntityRepository extends JpaRepository<WorkflowApprovalEntity, UUID>,WorkflowApprovalEntityRepositoryCustom {
    WorkflowApprovalEntity findByEntityTypeAndEntityIdAndApproverId(String entityType, UUID entityId, Long approverId);

    List<WorkflowApprovalEntity> findByEntityId(UUID applicationId);

    List<WorkflowApprovalEntity> findByApproverIdAndAction(Long approverId, String action);

    List<WorkflowApprovalEntity> findByApproverIdAndActionAndEntityType(Long approverId, String action, String entityType);

    List<WorkflowApprovalEntity> findByApproverIdAndEntityType(Long approverId, String entityType);

    List<WorkflowApprovalEntity> findByEntityType(String entityType);

    List<WorkflowApprovalEntity> findByEntityIdAndEntityType(UUID applicationId, String workflowInterviewsEntityType);


    List<WorkflowApprovalEntity> findByEntityIdAndEntityTypeAndAction(UUID applicationId, String workflowInterviewsEntityType, String workflowInterviewerApproverFeedback);
    WorkflowApprovalEntity findFirstByEntityTypeAndEntityIdOrderByStepNumberDesc(String entityType, UUID entityId);

    List<WorkflowApprovalEntity> findByEntityIdOrderByActionDateDesc(UUID entityId);

}