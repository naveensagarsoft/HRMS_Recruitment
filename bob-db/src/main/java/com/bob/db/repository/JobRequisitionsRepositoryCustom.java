package com.bob.db.repository;

import com.bob.db.entity.JobRequisitionsEntity;

import java.util.List;

public interface JobRequisitionsRepositoryCustom {
    JobRequisitionsEntity saveWithAudit(JobRequisitionsEntity oldEntity, JobRequisitionsEntity newEntity);
    List<JobRequisitionsEntity> saveAllWithAudit(List<JobRequisitionsEntity> oldEntities, List<JobRequisitionsEntity> newEntities);
}
