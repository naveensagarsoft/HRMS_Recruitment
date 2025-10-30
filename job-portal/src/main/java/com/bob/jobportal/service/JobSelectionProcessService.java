package com.bob.jobportal.service;

import com.bob.db.entity.JobSelectionProcessEntity;
import com.bob.db.repository.JobSelectionProcessRepository;
import com.bob.jobportal.model.JobPositionsModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class JobSelectionProcessService {

    @Autowired
    private JobSelectionProcessRepository jobSelectionProcessRepository;

    public JobSelectionProcessEntity setValues(JobPositionsModel jobPositionsDTO, UUID position_id){
        return JobSelectionProcessEntity.builder()
                .selectionProcedure(jobPositionsDTO.getSelectionProcedure())
                .positionId(position_id)
                .build();
    }

    public JobSelectionProcessEntity createSelectionProcess(JobPositionsModel jobPositionsDTO, UUID position_id){

        JobSelectionProcessEntity jobSelectionProcess = setValues(jobPositionsDTO,position_id);
        return jobSelectionProcessRepository.save(jobSelectionProcess);
    }

    public JobSelectionProcessEntity getByPositionIdSelectionProcess(UUID position_id){
        return jobSelectionProcessRepository.findByPositionId(position_id);
    }

    //updateSelectionProcess
    public JobSelectionProcessEntity updateSelectionProcess(JobPositionsModel jobPositionsDTO, UUID position_id) {
        JobSelectionProcessEntity existingProcess = getByPositionIdSelectionProcess(position_id);
        if (existingProcess != null) {
            existingProcess.setSelectionProcedure(jobPositionsDTO.getSelectionProcedure());
            return jobSelectionProcessRepository.save(existingProcess);
        }
        return null; // or throw an exception if not found
    }


}