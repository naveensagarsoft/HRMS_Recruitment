package com.bob.db.repository;

import com.bob.db.entity.AuditTrailEntity;
import com.bob.db.entity.JobRequisitionsEntity;
import com.bob.db.util.AppConstants;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Field;
import java.util.*;

public class JobRequisitionsRepositoryImpl implements JobRequisitionsRepositoryCustom {

    private static final Logger logger = LoggerFactory.getLogger(JobRequisitionsRepositoryImpl.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private AuditTrailEntityRepository auditTrailEntityRepository;

    @Override
    @Transactional
    public JobRequisitionsEntity saveWithAudit(JobRequisitionsEntity oldEntity, JobRequisitionsEntity newEntity) {
        JobRequisitionsEntity savedEntity = entityManager.merge(newEntity);
        // Fields to monitor for changes
        List<String> monitoredFields = Arrays.asList(
                "requisitionStatus",
                "requisitionTitle",
                "requisitionDescription",
                "requisitionComments",
                "requisitionApproval",
                "requisitionApprovalNotes",
                "noOfPositions",
                "jobPostings"
        );
        List<AuditTrailEntity> audits = new ArrayList<>();
        for (String fieldName : monitoredFields) {
            try {
                Field field = JobRequisitionsEntity.class.getDeclaredField(fieldName);
                field.setAccessible(true);
                Object oldValue = oldEntity != null ? field.get(oldEntity) : null;
                Object newValue = field.get(newEntity);
                boolean changed = (oldEntity == null && newValue != null) || (oldEntity != null && (oldValue == null ? newValue != null : !oldValue.equals(newValue)));
                if (changed) {
                    AuditTrailEntity audit = AuditTrailEntity.builder()
                            .changeType(oldEntity == null ? AppConstants.AUDIT_CHANGE_TYPE_CREATE : AppConstants.AUDIT_CHANGE_TYPE_UPDATE)
                            .entityId(savedEntity.getRequisitionId())
                            .entityType(JobRequisitionsEntity.ENTITY_TYPE)
                            .fieldChanged(fieldName)
                            .newValue(newValue != null ? newValue.toString() : null)
                            .oldValue(oldValue != null ? oldValue.toString() : null)
                            .changedBy(1L)//TODO: get from auth header
                            .build();
                    audits.add(audit);
                }
            } catch (NoSuchFieldException | IllegalAccessException e) {
                logger.error("Error accessing field '{}' for audit: {}", fieldName, e.getMessage(), e);
            }
        }
        if (!audits.isEmpty()) {
            auditTrailEntityRepository.saveAll(audits);
        }
        return savedEntity;
    }

    @Override
    @Transactional
    public List<JobRequisitionsEntity> saveAllWithAudit(List<JobRequisitionsEntity> oldEntities, List<JobRequisitionsEntity> newEntities) {
        Map<Object, JobRequisitionsEntity> oldEntityMap = new HashMap<>();
        if (oldEntities != null) {
            for (JobRequisitionsEntity oldEntity : oldEntities) {
                if (oldEntity != null && oldEntity.getRequisitionId() != null) {
                    oldEntityMap.put(oldEntity.getRequisitionId(), oldEntity);
                }
            }
        }
        List<JobRequisitionsEntity> savedEntities = new ArrayList<>();
        List<AuditTrailEntity> allAudits = new ArrayList<>();
        List<String> monitoredFields = Arrays.asList(
                "requisitionStatus",
                "requisitionTitle",
                "requisitionDescription",
                "requisitionComments",
                "requisitionApproval",
                "requisitionApprovalNotes",
                "noOfPositions",
                "jobPostings"
        );
        for (JobRequisitionsEntity newEntity : newEntities) {
            JobRequisitionsEntity oldEntity = newEntity.getRequisitionId() != null ? oldEntityMap.get(newEntity.getRequisitionId()) : null;
            JobRequisitionsEntity savedEntity = entityManager.merge(newEntity);
            savedEntities.add(savedEntity);
            for (String fieldName : monitoredFields) {
                try {
                    Field field = JobRequisitionsEntity.class.getDeclaredField(fieldName);
                    field.setAccessible(true);
                    Object oldValue = oldEntity != null ? field.get(oldEntity) : null;
                    Object newValue = field.get(newEntity);
                    boolean changed = (oldEntity == null && newValue != null) || (oldEntity != null && (oldValue == null ? newValue != null : !oldValue.equals(newValue)));
                    if (changed) {
                        AuditTrailEntity audit = AuditTrailEntity.builder()
                                .changeType(oldEntity == null ? AppConstants.AUDIT_CHANGE_TYPE_CREATE : AppConstants.AUDIT_CHANGE_TYPE_UPDATE)
                                .entityId(savedEntity.getRequisitionId())
                                .entityType(JobRequisitionsEntity.ENTITY_TYPE)
                                .fieldChanged(fieldName)
                                .newValue(newValue != null ? newValue.toString() : null)
                                .oldValue(oldValue != null ? oldValue.toString() : null)
                                .changedBy(1L)//TODO: get from auth header
                                .build();
                        allAudits.add(audit);
                    }
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    logger.error("Error accessing field '{}' for audit: {}", fieldName, e.getMessage(), e);
                }
            }
        }
        if (!allAudits.isEmpty()) {
            auditTrailEntityRepository.saveAll(allAudits);
        }
        return savedEntities;
    }
}
