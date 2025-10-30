package com.bob.jobportal.service;

import com.bob.db.dto.JobRequisitionsDTO;
import com.bob.db.entity.*;
import com.bob.db.repository.*;
import com.bob.jobportal.model.JobPositionsModel;
import com.bob.jobportal.util.AppConstants;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class JobPositionService{

    @Autowired
    private JobAgeRelaxationService jobAgeRelaxationService;

    @Autowired
    private JobGradeRepository jobGradeRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private DepartmentsRepository departmentsRepository;

    @Autowired
    private JobApplicationFeeService jobApplicationFeeService;

    @Autowired
    private JobPostingLocationService jobPostingLocationService;

    @Autowired
    private JobSelectionProcessService jobSelectionProcessService;

    @Autowired
    private JobVacanciesService jobVacanciesService;
    @Autowired
    private PositionsRepository positionsRepository;

    @Autowired
    private JobRelaxationPolicyRepository jobRelaxationPolicyRepository;

    //microservices
    private final JobRequisitionsService jobRequisitionsService;

    public JobPositionService(@Lazy JobRequisitionsService jobRequisitionsService) {
        this.jobRequisitionsService = jobRequisitionsService;
    }


    private JobPositionsModel setValuesDTO(
            PositionsEntity positions,
            JobPostingLocationEntity jobPostingLocation,
            JobVacanciesEntity jobVacancies,
            JobSelectionProcessEntity jobSelectionProcess,
            List<DepartmentsEntity> departments,
            List<LocationEntity> locations,
            List<JobGradeEntity> jobGrades
    ) {
        // Find job grade
        String gradeName = jobGrades.stream()
                .filter(grade -> (grade.getJobGradeId().intValue() == positions.getGradeId()))
                .findFirst()
                .map(grade -> String.format("%s (%d - %d)",
                        grade.getJobGradeCode(),
                        grade.getMinSalary().intValue(),
                        grade.getMaxSalary().intValue()))
                .orElse(null);

        // Find department name (only if jobPostingLocation is not null)
        String deptName = (jobPostingLocation == null) ? null :
                departments.stream()
                        .filter(dept -> dept.getDepartmentId() != null
                                && jobPostingLocation.getDeptId() != null
                                && dept.getDepartmentId().intValue() == jobPostingLocation.getDeptId())
                        .map(DepartmentsEntity::getDepartmentName)
                        .findFirst()
                        .orElse(null);

        // Find location name (only if jobPostingLocation is not null)
        String locationName = (jobPostingLocation == null) ? null :
                locations.stream()
                        .filter(loc -> loc.getLocationId().equals(jobPostingLocation.getLocationId()))
                        .map(LocationEntity::getLocationName)
                        .findFirst()
                        .orElse(null);

        return JobPositionsModel.builder()
                .positionId(positions.getPositionId())
                .requisitionId(positions.getRequisitionId())
                .positionTitle(positions.getPositionTitle())
                .description(positions.getDescription())
                .rolesResponsibilities(positions.getRolesResponsibilities())
                .gradeId(positions.getGradeId())
                .employmentType(positions.getEmploymentType())
                .eligibilityAgeMin(positions.getEligibilityAgeMin())
                .eligibilityAgeMax(positions.getEligibilityAgeMax())
                .mandatoryQualification(positions.getMandatoryQualification())
                .preferredQualification(positions.getPreferredQualification())
                .mandatoryExperience(positions.getMandatoryExperience())
                .preferredExperience(positions.getPreferredExperience())
                .probationPeriod(positions.getProbationPeriod())
                .documentsRequired(positions.getDocumentsRequired())
                .minCreditScore(positions.getMinCreditScore())
                .maxSalary(positions.getMaxSalary())
                .minSalary(positions.getMinSalary())
                .positionCode(positions.getPositionCode())
                .positionStatus(positions.getPositionStatus())
                .locationId(jobPostingLocation == null ? null : jobPostingLocation.getLocationId())
                .cityId(jobPostingLocation == null ? null : jobPostingLocation.getCityId())
                .countryId(jobPostingLocation == null ? null : jobPostingLocation.getCountryId())
                .stateId(jobPostingLocation == null ? null : jobPostingLocation.getStateId())
                .deptId(jobPostingLocation == null ? null : jobPostingLocation.getDeptId())
                .selectionProcedure(jobSelectionProcess == null ? null : jobSelectionProcess.getSelectionProcedure())
                .noOfVacancies(jobVacancies == null ? null : jobVacancies.getNoOfVacancies())
                .specialCatId(jobVacancies == null ? null : jobVacancies.getSpecialCatId())
                .reservationCatId(jobVacancies == null ? null : jobVacancies.getReservationCatId())
                .deptName(deptName)
                .gradeName(gradeName)
                .locationName(locationName)
                .jobRelaxationPolicyId(positions.getJobRelaxationPolicyId())
                .build();
    }


    private PositionsEntity setValues(JobPositionsModel positionsDTO){
        return PositionsEntity.builder()
                .positionId(positionsDTO.getPositionId())
                .positionCode(positionsDTO.getPositionCode())
                .requisitionId(positionsDTO.getRequisitionId())
                .positionTitle(positionsDTO.getPositionTitle())
                .description(positionsDTO.getDescription())
                .rolesResponsibilities(positionsDTO.getRolesResponsibilities())
                .gradeId(positionsDTO.getGradeId())
                .maxSalary(positionsDTO.getMaxSalary())
                .minSalary(positionsDTO.getMinSalary())
                .employmentType(positionsDTO.getEmploymentType())
                .eligibilityAgeMin(positionsDTO.getEligibilityAgeMin())
                .eligibilityAgeMax(positionsDTO.getEligibilityAgeMax())
                .mandatoryQualification(positionsDTO.getMandatoryQualification())
                .preferredQualification(positionsDTO.getPreferredQualification())
                .mandatoryExperience(positionsDTO.getMandatoryExperience())
                .preferredExperience(positionsDTO.getPreferredExperience())
                .probationPeriod(positionsDTO.getProbationPeriod())
                .documentsRequired(positionsDTO.getDocumentsRequired())
                .minCreditScore(positionsDTO.getMinCreditScore())
                .positionStatus(AppConstants.JOB_POSITION_STATUS_ACTIVE)
                .isActive(true)
                .jobRelaxationPolicyId(positionsDTO.getJobRelaxationPolicyId())
                .createdBy("") // TODO: review this and fix with frontend team
                .updatedBy("")
                .build();
    }

    // save to repo
    @Transactional
    public JobPositionsModel createPosition(JobPositionsModel jobPositionsDTO){

        PositionsEntity positions = setValues(jobPositionsDTO);
        PositionsEntity positions1 =positionsRepository.save(positions);

        UUID position_id = positions1.getPositionId();

        //posting location - service
        JobPostingLocationEntity jobPostingLocation = jobPostingLocationService.createPostingLocation(jobPositionsDTO,position_id);

        //selection process - service
        JobSelectionProcessEntity jobSelectionProcess =jobSelectionProcessService.createSelectionProcess(jobPositionsDTO,position_id);

        //vacancies - services
        JobVacanciesEntity jobVacancies = jobVacanciesService.createJobVacancies(jobPositionsDTO,position_id);
        List<DepartmentsEntity> departments = departmentsRepository.findAllByIsActiveTrue();
        List<LocationEntity> locations = locationRepository.findAllByIsActiveTrue();
        List<JobGradeEntity> jobGrades = jobGradeRepository.findAllByIsActiveTrue();
        return setValuesDTO(positions1,jobPostingLocation,jobVacancies,jobSelectionProcess,departments, locations, jobGrades);
    }
    // Bulk creation
    @Transactional
    public List<JobPositionsModel> createBulkPostions(List<JobPositionsModel> jobPositionsDTOS){
        List<JobPositionsModel> jobPositionsDTOList = new ArrayList<>();
        for(JobPositionsModel jobs: jobPositionsDTOS){
            jobPositionsDTOList.add(createPosition(jobs));
        }
        return jobPositionsDTOList;
    }

    // get by recId
    @Transactional
    public List<JobPositionsModel> findByReqId(UUID requisition_id){

        List<PositionsEntity> jobposition = positionsRepository.findByRequisitionIdAndIsActive(requisition_id,true);
        List<JobPositionsModel> result = new ArrayList<>();
        List<DepartmentsEntity> departments = departmentsRepository.findAllByIsActiveTrue();
        List<LocationEntity> locations = locationRepository.findAllByIsActiveTrue();
        List<JobGradeEntity> jobGrades = jobGradeRepository.findAllByIsActiveTrue();
        List<UUID> jobRelaxationPolicyIds = jobposition.stream()
                .map(PositionsEntity::getJobRelaxationPolicyId)
                .distinct()
                .toList();
        List<JobRelaxationPolicyEntity> jobRelaxationPolicies = jobRelaxationPolicyRepository.findAllById(jobRelaxationPolicyIds);
        for(PositionsEntity position :jobposition ){
            if (position.getPositionStatus().equals(AppConstants.JOB_POSITION_STATUS_ACTIVE) ){
                UUID position_id = position.getPositionId();
                JobPostingLocationEntity jobPostingLocation = jobPostingLocationService.getByPositionId(position_id);
                JobSelectionProcessEntity jobSelectionProcess = jobSelectionProcessService.getByPositionIdSelectionProcess(position_id);
                JobVacanciesEntity jobVacancies = jobVacanciesService.getByPositionIdJobVacancies(position_id);
                JobRelaxationPolicyEntity jobRelaxationPolicy = jobRelaxationPolicies.stream()
                        .filter(policy -> policy.getJobRelaxationPolicyId().equals(position.getJobRelaxationPolicyId()))
                        .findFirst()
                        .orElse(null);
                JobPositionsModel jobPositionsDTO = setValuesDTO(position,jobPostingLocation,jobVacancies,jobSelectionProcess, departments, locations, jobGrades);

                jobPositionsDTO.setJobRelaxationPolicyJson(jobRelaxationPolicy == null ? null : jobRelaxationPolicy.getRelaxation());
                result.add(jobPositionsDTO);
            }
        }
        return result;
    }
    //get by posId
    @Transactional
    public JobPositionsModel findByPositionId(UUID position_id){
        PositionsEntity position = positionsRepository.findById(position_id).orElse(null);
        if (position == null) return null;
        JobRelaxationPolicyEntity jobRelaxationPolicy = jobRelaxationPolicyRepository.findById(position.getJobRelaxationPolicyId()).orElse(null);
        if (position.getPositionStatus().equals(AppConstants.JOB_POSITION_STATUS_ACTIVE) && position.getIsActive().equals(true) ) {
            JobPostingLocationEntity jobPostingLocation = jobPostingLocationService.getByPositionId(position_id);
            JobSelectionProcessEntity jobSelectionProcess = jobSelectionProcessService.getByPositionIdSelectionProcess(position_id);
            JobVacanciesEntity jobVacancies = jobVacanciesService.getByPositionIdJobVacancies(position_id);
            List<DepartmentsEntity> departments = departmentsRepository.findAllByIsActiveTrue();
            List<LocationEntity> locations = locationRepository.findAllByIsActiveTrue();
            List<JobGradeEntity> jobGrades = jobGradeRepository.findAllByIsActiveTrue();
            JobPositionsModel jobPositionsDTO = setValuesDTO(position, jobPostingLocation,  jobVacancies, jobSelectionProcess, departments, locations, jobGrades);
            jobPositionsDTO.setJobRelaxationPolicyJson(jobRelaxationPolicy == null ? null : jobRelaxationPolicy.getRelaxation());
            return jobPositionsDTO;
        }
        return null;
    }
    //get all
        public List<JobPositionsModel> findAllPositions(){
        List<PositionsEntity> positionsList = positionsRepository.findAllByIsActiveTrue();
        List<JobPositionsModel> jobPositionsDTOList = new ArrayList<>();
        List<DepartmentsEntity> departments = departmentsRepository.findAllByIsActiveTrue();
        List<LocationEntity> locations = locationRepository.findAllByIsActiveTrue();
        List<JobGradeEntity> jobGrades = jobGradeRepository.findAllByIsActiveTrue();
        for(PositionsEntity positions:positionsList) {
            if (positions.getPositionStatus().equals(AppConstants.JOB_POSITION_STATUS_ACTIVE)) {
                UUID position_id = positions.getPositionId();
                JobPostingLocationEntity jobPostingLocation = jobPostingLocationService.getByPositionId(position_id);
                JobSelectionProcessEntity jobSelectionProcess = jobSelectionProcessService.getByPositionIdSelectionProcess(position_id);
                JobVacanciesEntity jobVacancies = jobVacanciesService.getByPositionIdJobVacancies(position_id);
                JobPositionsModel jobPositionsDTO = setValuesDTO(positions, jobPostingLocation, jobVacancies,  jobSelectionProcess, departments, locations, jobGrades);
                jobPositionsDTOList.add(jobPositionsDTO);
            }
        }
        return jobPositionsDTOList;
    }


    // update by posId
    public JobPositionsModel updateJobposition(JobPositionsModel jobPositionsDTO){
        UUID positionId = jobPositionsDTO.getPositionId();
        PositionsEntity existingPosition = positionsRepository.findById(positionId).orElse(null);
        if (existingPosition == null) {
            return null; // or throw an exception
        }
        // Update the existing position with new values
        existingPosition.setRequisitionId(jobPositionsDTO.getRequisitionId());
        existingPosition.setPositionTitle(jobPositionsDTO.getPositionTitle());
        existingPosition.setDescription(jobPositionsDTO.getDescription());
        existingPosition.setRolesResponsibilities(jobPositionsDTO.getRolesResponsibilities());
        existingPosition.setGradeId(jobPositionsDTO.getGradeId());
        existingPosition.setEmploymentType(jobPositionsDTO.getEmploymentType());
        existingPosition.setEligibilityAgeMin(jobPositionsDTO.getEligibilityAgeMin());
        existingPosition.setEligibilityAgeMax(jobPositionsDTO.getEligibilityAgeMax());
        existingPosition.setMandatoryQualification(jobPositionsDTO.getMandatoryQualification());
        existingPosition.setPreferredQualification(jobPositionsDTO.getPreferredQualification());
        existingPosition.setMandatoryExperience(jobPositionsDTO.getMandatoryExperience());
        existingPosition.setPreferredExperience(jobPositionsDTO.getPreferredExperience());
        existingPosition.setProbationPeriod(jobPositionsDTO.getProbationPeriod());
        existingPosition.setDocumentsRequired(jobPositionsDTO.getDocumentsRequired());
        existingPosition.setMinCreditScore(jobPositionsDTO.getMinCreditScore());
        existingPosition.setUpdatedBy(""); //TODO: check with frontend team
        existingPosition.setMaxSalary(jobPositionsDTO.getMaxSalary());
        existingPosition.setMinSalary(jobPositionsDTO.getMinSalary());
        existingPosition.setJobRelaxationPolicyId(jobPositionsDTO.getJobRelaxationPolicyId());
        List<DepartmentsEntity> departments = departmentsRepository.findAllByIsActiveTrue();
        List<LocationEntity> locations = locationRepository.findAllByIsActiveTrue();
        List<JobGradeEntity> jobGrades = jobGradeRepository.findAllByIsActiveTrue();
        // Save the updated position
        positionsRepository.save(existingPosition);

        // posting location
        JobPostingLocationEntity jobPostingLocation = jobPostingLocationService.getByPositionId(positionId);
        if (jobPostingLocation == null) {
            jobPostingLocation = jobPostingLocationService.createPostingLocation(jobPositionsDTO, positionId);
        } else {
            jobPostingLocationService.updatePostingLocation(jobPositionsDTO, positionId);
        }
        //selection process
        JobSelectionProcessEntity jobSelectionProcess = jobSelectionProcessService.getByPositionIdSelectionProcess(positionId);
        if (jobSelectionProcess == null) {
            jobSelectionProcess = jobSelectionProcessService.createSelectionProcess(jobPositionsDTO, positionId);
        } else {
            jobSelectionProcessService.updateSelectionProcess(jobPositionsDTO, positionId);
        }
        //job vacancies
        JobVacanciesEntity jobVacancies = jobVacanciesService.getByPositionIdJobVacancies(positionId);
        if (jobVacancies == null) {
            jobVacancies = jobVacanciesService.createJobVacancies(jobPositionsDTO, positionId);
        } else {
            jobVacanciesService.updateJobVacancies(jobPositionsDTO, positionId);
        }
        // Always return a new JobPositionsModel using the builder pattern
        return setValuesDTO(existingPosition, jobPostingLocation, jobVacancies, jobSelectionProcess, departments, locations, jobGrades);
    }


    public String deleteByPositionId(UUID positionId){
        JobPositionsModel positions = findByPositionId(positionId);
        if (positions == null) {
            return "Position not found";
        }
        PositionsEntity position = positionsRepository.findById(positionId).orElse(null);
        if (position != null) {
            position.setPositionStatus(AppConstants.JOB_POSITION_STATUS_INACTIVE);
            position.setIsActive(false);
            positionsRepository.save(position);
            return "Position deleted successfully";
        } else {
            return "Position not found";
        }
    }

    @Transactional
    public List<JobPositionsModel> getActiveJobs() {
        List<JobRequisitionsDTO> activeRequisitions = jobRequisitionsService.getActiveRequisitions();
        List<JobPositionsModel> activePositions = new ArrayList<>();
        List<DepartmentsEntity> departments = departmentsRepository.findAllByIsActiveTrue();
        List<LocationEntity> locations = locationRepository.findAllByIsActiveTrue();
        List<JobGradeEntity> jobGrades = jobGradeRepository.findAllByIsActiveTrue();
        List<UUID> requisitionIds = activeRequisitions.stream()
                .map(JobRequisitionsDTO::getRequisitionId)
                .toList();
        for( JobRequisitionsDTO requisition : activeRequisitions) {
            List<PositionsEntity> positions = positionsRepository.findAllByRequisitionId(requisition.getRequisitionId());

            for (PositionsEntity position : positions) {
                if (position.getPositionStatus().equals("Active")) {
                    JobPostingLocationEntity jobPostingLocation = jobPostingLocationService.getByPositionId(position.getPositionId());
                    JobSelectionProcessEntity jobSelectionProcess = jobSelectionProcessService.getByPositionIdSelectionProcess(position.getPositionId());
                    JobVacanciesEntity jobVacancies = jobVacanciesService.getByPositionIdJobVacancies(position.getPositionId());
                    JobPositionsModel jobPositionsDTO = setValuesDTO(position, jobPostingLocation, jobVacancies, jobSelectionProcess, departments, locations, jobGrades);
                    jobPositionsDTO.setRequisitionCode(requisition.getRequisitionCode());
                    jobPositionsDTO.setRequisitionTitle(requisition.getRequisitionTitle());
                    activePositions.add(jobPositionsDTO);
                }
            }
        }

        activePositions.sort((p1, p2) ->
                p2.getRequisitionCode().compareTo(p1.getRequisitionCode())
        );

        return activePositions;
    }

}