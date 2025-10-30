package com.bob.db.repository;

import com.bob.db.entity.CandidateApplicationsEntity;

public interface CandidateApplicationsRepositoryCustom {
    CandidateApplicationsEntity saveWithWorkflow(CandidateApplicationsEntity newEntity);
}
