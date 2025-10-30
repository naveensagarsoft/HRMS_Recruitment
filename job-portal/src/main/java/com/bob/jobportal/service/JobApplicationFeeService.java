package com.bob.jobportal.service;

import com.bob.db.entity.JobApplicationFeeEntity;
import com.bob.db.repository.JobApplicationFeeRepository;
import com.bob.jobportal.model.JobPositionsModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class JobApplicationFeeService {

    //repo
    @Autowired
    private JobApplicationFeeRepository jobApplicationFeeRepository;

    private JobApplicationFeeEntity setValues(JobPositionsModel jobPositionsDTO, UUID position_id){
        return JobApplicationFeeEntity.builder()
                .positionId(position_id)
                .build();
    }

    public JobApplicationFeeEntity createApplicationFee(JobPositionsModel jobPositionsDTO, UUID position_id){
        JobApplicationFeeEntity jobApplicationFee = setValues(jobPositionsDTO,position_id);
        return jobApplicationFeeRepository.save(jobApplicationFee);
    }

    public JobApplicationFeeEntity getByPositionIdApplicationFee(UUID position_id){
        return jobApplicationFeeRepository.findByPositionId(position_id);
    }

}