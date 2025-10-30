package com.bob.jobportal.service;

import com.bob.db.entity.JobAgeRelaxationsEntity;
import com.bob.db.repository.JobAgeRelaxationsRepository;
import com.bob.jobportal.model.JobPositionsModel;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class JobAgeRelaxationService {

    //Repo
    @Autowired
    private JobAgeRelaxationsRepository jobAgeRelaxationsRepository;

    private JobAgeRelaxationsEntity setValues(JobPositionsModel jobPositionsDTO, UUID position_id){
        return JobAgeRelaxationsEntity.builder()
                .positionId(position_id)
                .ageRelaxationId(1)
                .build();
    }

    @Transactional
    public JobAgeRelaxationsEntity createAgeRelaxation(JobPositionsModel jobPositionsDTO, UUID position_id){
        JobAgeRelaxationsEntity jobAgeRelaxations = setValues(jobPositionsDTO,position_id);
        return jobAgeRelaxationsRepository.save(jobAgeRelaxations);
    }

    @Transactional
    public JobAgeRelaxationsEntity getByPositionIdAgeRelaxation(UUID position_id){
        return jobAgeRelaxationsRepository.findByPositionId(position_id);
    }
}