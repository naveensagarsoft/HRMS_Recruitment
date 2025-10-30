package com.bob.db.repository;

import com.bob.db.entity.AuditTrailEntity;
import com.bob.db.entity.InterviewsEntity;
import com.bob.db.util.AppConstants;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Field;
import java.util.*;

public class InterviewsRepositoryImpl implements InterviewsRepositoryCustom {

    private static final Logger logger = LoggerFactory.getLogger(InterviewsRepositoryImpl.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private AuditTrailEntityRepository auditTrailEntityRepository;

    @Override
    @Transactional
    public InterviewsEntity saveWithAudit(InterviewsEntity oldEntity, InterviewsEntity newEntity) {
        InterviewsEntity savedEntity = entityManager.merge(newEntity);
        List<String> monitoredFields = Arrays.asList(
                "scheduledAt",
                "time",
                "status",
                "interviewerId"
        );
        List<AuditTrailEntity> audits = new ArrayList<>();
        for (String fieldName : monitoredFields) {
            try {
                Field field = InterviewsEntity.class.getDeclaredField(fieldName);
                field.setAccessible(true);
                Object oldValue = oldEntity != null ? field.get(oldEntity) : null;
                Object newValue = field.get(newEntity);
                boolean changed = (oldEntity == null && newValue != null) || (oldEntity != null && (oldValue == null ? newValue != null : !oldValue.equals(newValue)));
                if (changed) {
                    AuditTrailEntity audit = AuditTrailEntity.builder()
                            .changeType(oldEntity == null ? AppConstants.AUDIT_CHANGE_TYPE_CREATE : AppConstants.AUDIT_CHANGE_TYPE_UPDATE)
                            .entityId(savedEntity.getInterviewId())
                            .entityType(InterviewsEntity.ENTITY_TYPE)
                            .fieldChanged(fieldName)
                            .newValue(newValue != null ? newValue.toString() : null)
                            .oldValue(oldValue != null ? oldValue.toString() : null)
                            .changedBy(1L) // TODO: get from auth header
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
    public List<InterviewsEntity> saveAllWithAudit(List<InterviewsEntity> oldEntities, List<InterviewsEntity> newEntities) {
        Map<Object, InterviewsEntity> oldEntityMap = new HashMap<>();
        if (oldEntities != null) {
            for (InterviewsEntity oldEntity : oldEntities) {
                if (oldEntity != null && oldEntity.getInterviewId() != null) {
                    oldEntityMap.put(oldEntity.getInterviewId(), oldEntity);
                }
            }
        }
        List<InterviewsEntity> savedEntities = new ArrayList<>();
        List<AuditTrailEntity> allAudits = new ArrayList<>();
        List<String> monitoredFields = Arrays.asList(
                "applicationId",
                "type",
                "scheduledAt",
                "time",
                "status",
                "interviewerId"
        );
        for (InterviewsEntity newEntity : newEntities) {
            InterviewsEntity oldEntity = newEntity.getInterviewId() != null ? oldEntityMap.get(newEntity.getInterviewId()) : null;
            InterviewsEntity savedEntity = entityManager.merge(newEntity);
            savedEntities.add(savedEntity);
            for (String fieldName : monitoredFields) {
                try {
                    Field field = InterviewsEntity.class.getDeclaredField(fieldName);
                    field.setAccessible(true);
                    Object oldValue = oldEntity != null ? field.get(oldEntity) : null;
                    Object newValue = field.get(newEntity);
                    boolean changed = (oldEntity == null && newValue != null) || (oldEntity != null && (oldValue == null ? newValue != null : !oldValue.equals(newValue)));
                    if (changed) {
                        AuditTrailEntity audit = AuditTrailEntity.builder()
                                .changeType(oldEntity == null ? AppConstants.AUDIT_CHANGE_TYPE_CREATE : AppConstants.AUDIT_CHANGE_TYPE_UPDATE)
                                .entityId(savedEntity.getInterviewId())
                                .entityType("InterviewsEntity")
                                .fieldChanged(fieldName)
                                .newValue(newValue != null ? newValue.toString() : null)
                                .oldValue(oldValue != null ? oldValue.toString() : null)
                                .changedBy(1L) // TODO: get from auth header
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

