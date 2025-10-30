package com.bob.jobportal.service;

import com.bob.db.entity.JobVacanciesEntity;
import com.bob.db.repository.JobVacanciesRepository;
import com.bob.jobportal.model.JobPositionsModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class JobVacanciesService {

    @Autowired
    private JobVacanciesRepository jobVacanciesRepository;

    public JobVacanciesEntity setValues(JobPositionsModel jobPositionsDTO, UUID position_id){
        return JobVacanciesEntity.builder()
            .positionId(position_id)
            .noOfVacancies(jobPositionsDTO.getNoOfVacancies())
            .locationId(jobPositionsDTO.getLocationId())
            .reservationCatId(jobPositionsDTO.getReservationCatId())
            .specialCatId(jobPositionsDTO.getSpecialCatId())
            .build();
    }

    //save - jobVacancies
    public JobVacanciesEntity createJobVacancies(JobPositionsModel jobPositionsDTO, UUID position_id){
        JobVacanciesEntity jobVacancies = setValues(jobPositionsDTO,position_id);
        return jobVacanciesRepository.save(jobVacancies);
    }

    public JobVacanciesEntity getByPositionIdJobVacancies(UUID position_id){
        return jobVacanciesRepository.findByPositionId(position_id);
    }

    public void updateJobVacancies(JobPositionsModel jobPositionsDTO, UUID positionId) {
        JobVacanciesEntity existingVacancy = jobVacanciesRepository.findByPositionId(positionId);
        if (existingVacancy != null) {
            existingVacancy.setNoOfVacancies(jobPositionsDTO.getNoOfVacancies());
            existingVacancy.setLocationId(jobPositionsDTO.getLocationId());
            existingVacancy.setReservationCatId(jobPositionsDTO.getReservationCatId());
            existingVacancy.setSpecialCatId(jobPositionsDTO.getSpecialCatId());
            jobVacanciesRepository.save(existingVacancy);
        } else {
            throw new RuntimeException("Job vacancy not found for position ID: " + positionId);
        }
    }
}