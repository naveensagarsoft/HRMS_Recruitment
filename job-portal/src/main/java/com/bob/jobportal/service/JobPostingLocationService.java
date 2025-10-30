package com.bob.jobportal.service;


import com.bob.db.entity.JobPostingLocationEntity;
import com.bob.db.repository.*;
import com.bob.jobportal.model.JobPositionsModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class JobPostingLocationService {

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private StateRepository stateRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private JobPostingLocationRepository jobPostingLocationRepository;

    public JobPostingLocationEntity setValues(JobPositionsModel jobPositionsDTO, UUID positionId){
        return JobPostingLocationEntity.builder()
                .locationId(jobPositionsDTO.getLocationId())
                .deptId(jobPositionsDTO.getDeptId())
                .positionId(positionId)
                .cityId(jobPositionsDTO.getCityId())
                .countryId(jobPositionsDTO.getCountryId())
                .stateId(jobPositionsDTO.getStateId())
                .build();
    }

    public JobPostingLocationEntity createPostingLocation(JobPositionsModel jobPositionsDTO, UUID positionId) {
        JobPostingLocationEntity jobPostingLocation = setValues(jobPositionsDTO, positionId);
        return jobPostingLocationRepository.save(jobPostingLocation);
    }

    public JobPostingLocationEntity getByPositionId(UUID positionId) {
        return jobPostingLocationRepository.findByPositionId(positionId);
    }

    public void updatePostingLocation(JobPositionsModel jobPositionsDTO, UUID positionId) {
        JobPostingLocationEntity existingLocation = jobPostingLocationRepository.findByPositionId(positionId);
        if (existingLocation != null) {
            existingLocation.setDeptId(jobPositionsDTO.getDeptId());
            existingLocation.setLocationId(jobPositionsDTO.getLocationId());
            existingLocation.setCityId(jobPositionsDTO.getCityId());
            existingLocation.setStateId(jobPositionsDTO.getStateId());
            existingLocation.setCountryId(jobPositionsDTO.getCountryId());
            jobPostingLocationRepository.save(existingLocation);
        } else {
            throw new RuntimeException("Posting location not found for position ID: " + positionId);
        }
    }

    public Long findCityBylocationId(Long locationId) {
        return locationRepository.findByLocationId(locationId).getCityId();
    }
    public Long findStateByCityId(Long cityId) {
        return cityRepository.findStateIdByCityId(cityId);
    }
    public Long findCountryByStateId(Long stateId) {
        return stateRepository.findByStateId(stateId).getCountryId();
    }

}