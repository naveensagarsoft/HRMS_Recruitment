package com.bob.jobportal.service;

import com.bob.db.dto.JobRequisitionsDTO;
import com.bob.db.dto.WorkflowApprovalDTO;
import com.bob.db.entity.*;
import com.bob.db.mapper.JobRequisitionMapper;
import com.bob.db.mapper.WorkflowApprovalMapper;
import com.bob.db.repository.*;
import com.bob.jobportal.model.ApproveJobPostingsRequest;
import com.bob.jobportal.model.JobPostingSubmissionRequest;
import com.bob.jobportal.model.JobRequisitionModel;
import com.bob.jobportal.model.WorkflowApprovalsDetailsModel;
import com.bob.jobportal.util.AppConstants;
import com.bob.jobportal.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class JobRequisitionsService {

    @Autowired
    private JobRequisitionsRepository jobRequisitionsRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WorkflowApprovalEntityRepository workflowApprovalEntityRepository;

    @Autowired
    private JobVacanciesRepository jobVacanciesRepository;
    @Autowired
    private PositionsRepository positionsRepository;

    @Autowired
    private CandidateApplicationsRepository candidateApplicationsRepository;
    @Autowired
    private JobRequisitionMapper jobRequisitionMapper;
    @Autowired
    private WorkflowApprovalMapper workflowApprovalMapper;

    @Autowired
    private SecurityUtils securityUtils;


    public List<JobRequisitionModel> getAll() {
        List<JobRequisitionsEntity> jobRequisitions = jobRequisitionsRepository.findAllByIsactiveOrderByRequisitionCodeDesc(1);

        if (!securityUtils.isAdmin()) {
            List<JobRequisitionsEntity> filteredJobs = jobRequisitions.stream()
                    .filter(job -> job.getCreatedBy() != null)
                    .filter(job -> job.getCreatedBy().equals(securityUtils.getCurrentUserToken()))
                    .collect(Collectors.toList());

            jobRequisitions = filteredJobs;
        }

        if( jobRequisitions.isEmpty()){
            return null;
        }

        List<UUID> reqIds = jobRequisitions.stream().map(JobRequisitionsEntity::getRequisitionId).toList();
        List<PositionsEntity> positions = positionsRepository.findByRequisitionIdIn(reqIds);
        List<UUID> posIds = positions.stream().filter(positionsEntity -> positionsEntity.getPositionStatus().equals("Active")).map(PositionsEntity::getPositionId).toList();
        List<JobVacanciesEntity> vacancies = jobVacanciesRepository.findByPositionIdIn(posIds);

        List<CandidateApplicationsEntity> applications = candidateApplicationsRepository.findAll().stream().filter(
                application -> posIds.contains(application.getPositionId())

        ).collect(Collectors.toList());
        if (jobRequisitions.isEmpty()) {
            return null;
        }
        List<JobRequisitionModel> jobRequisitionModels = jobRequisitions.stream()
                .map(jobRequisition -> JobRequisitionModel.builder()
                        .requisitionId(jobRequisition.getRequisitionId())
                        .requisitionCode(jobRequisition.getRequisitionCode())
                        .requisitionTitle(jobRequisition.getRequisitionTitle())
                        .requisitionDescription(jobRequisition.getRequisitionDescription())
                        .registrationStartDate(jobRequisition.getRegistrationStartDate())
                        .registrationEndDate(jobRequisition.getRegistrationEndDate())
                        .requisitionStatus(jobRequisition.getRequisitionStatus())
                        .requisitionComments(jobRequisition.getRequisitionComments())
                        .requisitionApproval(jobRequisition.getRequisitionApproval())
                        .noOfPositions(jobRequisition.getNoOfPositions())
                        .jobPostings(jobRequisition.getJobPostings())
                        .requisitionApprovalNotes(jobRequisition.getRequisitionApprovalNotes())
                        .count(
                                vacancies.stream()
                                        .filter(vacancy -> positions.stream()
                                                .anyMatch(position -> position.getPositionId().equals(vacancy.getPositionId())
                                                        && position.getRequisitionId().equals(jobRequisition.getRequisitionId())))
                                        .mapToInt(JobVacanciesEntity::getNoOfVacancies)
                                        .sum()
                        )
                        .fullfillment(
                                (int) applications.stream()
                                        .filter(app -> {
                                            PositionsEntity pos = positions.stream()
                                                    .filter(p -> p.getPositionId().equals(app.getPositionId()))
                                                    .findFirst()
                                                    .orElse(null);
                                            return pos != null && pos.getRequisitionId().equals(jobRequisition.getRequisitionId());
                                        }).filter(app -> app.getApplicationStatus().equals(AppConstants.CANDIDATE_APPLICATION_STATUS_Offered))
                                        .count()
                        )
                        .build())
                .collect(Collectors.toList());

        return jobRequisitionModels;
    }

    public List<JobRequisitionsDTO> findByRequisitionStatus(String requisitionStatus) {

        List<JobRequisitionsEntity> jobRequisitions = jobRequisitionsRepository.findAllByRequisitionStatusAndIsactive(requisitionStatus,1);
        return jobRequisitionMapper.toDTOs(jobRequisitions);
    }

    public JobRequisitionsDTO createRequisitions(JobRequisitionsDTO jobRequisitions) {
        JobRequisitionsEntity jobRequisitionsEntity = jobRequisitionMapper.toEntity(jobRequisitions);
        jobRequisitionsEntity.setRequisitionCode("JREQ-" + (1000 + jobRequisitionsRepository.count()));
        jobRequisitionsEntity.setRequisitionStatus(AppConstants.REQ_APPROVAL_New);
        LocalDateTime tim = LocalDateTime.now();
        return jobRequisitionMapper.toDTO( jobRequisitionsRepository.saveWithAudit(null,jobRequisitionsEntity));
    }

    public List<JobRequisitionsDTO> createBulkRequistions(List<JobRequisitionsDTO> jobRequisitionsList) {
        List<JobRequisitionsEntity> jobRequisitionsListEntities = jobRequisitionMapper.toEntities(jobRequisitionsList);
        Long counter = jobRequisitionsRepository.count();
        LocalDateTime tim = LocalDateTime.now();
        for (JobRequisitionsEntity jobRequisitions : jobRequisitionsListEntities) {
            jobRequisitions.setRequisitionCode("JREQ-" + (1000 + counter++));
            jobRequisitions.setRequisitionStatus(AppConstants.REQ_APPROVAL_New);
        }
        return jobRequisitionMapper.toDTOs( jobRequisitionsRepository.saveAllWithAudit(null,jobRequisitionsListEntities));
    }

    public JobRequisitionsDTO updateRequisitions(JobRequisitionsDTO jobRequisitions) {
        JobRequisitionsEntity jobRequisitions1 = jobRequisitionsRepository.findById(jobRequisitions.getRequisitionId())
                .orElseThrow(() -> new RuntimeException("Requisition not found with ID: " + jobRequisitions.getRequisitionId()));
        JobRequisitionsEntity jobRequisitionsOld = jobRequisitions1.toBuilder().build();
        jobRequisitions1.setRequisitionId(jobRequisitions.getRequisitionId());

        jobRequisitions1.setRequisitionTitle(jobRequisitions.getRequisitionTitle());
        jobRequisitions1.setRequisitionDescription(jobRequisitions.getRequisitionDescription());

        jobRequisitions1.setRegistrationStartDate(jobRequisitions.getRegistrationStartDate());
        jobRequisitions1.setRegistrationEndDate(jobRequisitions.getRegistrationEndDate());
        jobRequisitions1.setRequisitionComments(jobRequisitions.getRequisitionComments());
        jobRequisitions1.setNoOfPositions(jobRequisitions.getNoOfPositions());
//        jobRequisitions.setUpdatedDate(LocalDateTime.now());
        return jobRequisitionMapper.toDTO(jobRequisitionsRepository.saveWithAudit(jobRequisitionsOld,jobRequisitions1));
    }

    public String deleteRequisitions(UUID id) {
        try {
            if (!jobRequisitionsRepository.existsById(id)) {
                return "Requisition not found";
            }
            //SOFT DELETING
            JobRequisitionsEntity jobRequisitions = jobRequisitionsRepository.findById(id).orElseThrow(() -> new RuntimeException("Requisition not found with ID: " + id));
            JobRequisitionsEntity jobRequisitionsOld = jobRequisitions.toBuilder().build();
            jobRequisitions.setIsactive(0);
            jobRequisitionsRepository.saveWithAudit(jobRequisitionsOld,jobRequisitions);
            return "Deleted Successful";
        } catch (Exception e) {
            return "Deleted Unsuccessful" + e.getMessage();
        }
    }

    public String createJobPostings(JobPostingSubmissionRequest jobPostingSubmissionRequest) throws Exception {
        try {
            for (UUID jobRequisitionsId : jobPostingSubmissionRequest.getRequisitionIds()) {
                if (!jobRequisitionsRepository.existsById(jobRequisitionsId)) {
                    return "Requisition with ID " + jobRequisitionsId + " does not exist.";
                }
            }

            String thirdPartyPostingsList = Optional.ofNullable(jobPostingSubmissionRequest.getJobPostings())
                    .orElse(Collections.emptyList())  // if null, use empty list
                    .stream()
                    .collect(Collectors.joining(","));

            if (jobPostingSubmissionRequest.getApprovalStatus().equals("Direct Approval")) {
                for (UUID jobRequisitionsId : jobPostingSubmissionRequest.getRequisitionIds()) {
                    JobRequisitionsEntity jobRequisitions = jobRequisitionsRepository.findById(jobRequisitionsId).orElse(null);
                    if (jobRequisitions != null) {
                        JobRequisitionsEntity jobRequisitionsOld = jobRequisitions.toBuilder().build();
                        jobRequisitions.setJobPostings(thirdPartyPostingsList);
                        jobRequisitions.setRequisitionStatus("Approved");
                        jobRequisitions.setRequisitionApproval("Direct Approval");
                        jobRequisitionsRepository.saveWithAudit(jobRequisitionsOld,jobRequisitions);
                    }
                }
            } else if (jobPostingSubmissionRequest.getApprovalStatus().equals(AppConstants.APPROVAL_STATUS_WORKFLOW)) {
                for (UUID jobRequisitionsId : jobPostingSubmissionRequest.getRequisitionIds()) {
                    JobRequisitionsEntity jobRequisitions = jobRequisitionsRepository.findById(jobRequisitionsId).orElse(null);
                    if (jobRequisitions != null) {
                        JobRequisitionsEntity jobRequisitionsOld = jobRequisitions.toBuilder().build();
                        long userId = jobPostingSubmissionRequest.getUserId();
                        userRepository.findById(userId)
                                .map(user -> user.getManagerId())
                                .flatMap(managerId -> userRepository.findById(managerId))
                                .ifPresentOrElse(manager -> {
                                    jobRequisitions.setJobPostings(thirdPartyPostingsList);
                                    jobRequisitions.setRequisitionStatus(AppConstants.REQ_PENDING);
                                    jobRequisitions.setRequisitionApproval(AppConstants.APPROVAL_STATUS_WORKFLOW);
                                    jobRequisitions.setNoOfApprovals(jobPostingSubmissionRequest.getNoOfApprovals());
                                    jobRequisitionsRepository.saveWithAudit(jobRequisitionsOld,jobRequisitions);
                                    createWorkflowApprovalPendingRecord(manager, jobRequisitions.getRequisitionId(), 1);
                                }, () -> {
                                    throw new RuntimeException("Manager User not found in users table.");
                                });
                    }
                }
            }

            return "Job postings created successfully ";
        } catch (Exception e) {
            throw new Exception("Failed to create job postings: " + e.getMessage());
        }
    }

    public boolean existsById(UUID id) {
        // Check if a job requisition with the given ID exists
        if (id == null) {
            return false; // or throw an exception, depending on your design choice
        }
        JobRequisitionsEntity jobRequisitions = jobRequisitionsRepository.findById(id).orElse(null);
        return (jobRequisitions != null && jobRequisitions.getIsactive() == 1) ? true : false;
    }

    public List<JobRequisitionsDTO> getActiveRequisitions() {
        LocalDate today = LocalDate.now();

        List<String> statuses = List.of(AppConstants.REQ_APPROVAL_PUBLISHED, AppConstants.REQ_APPROVAL_APPROVED);

        List<JobRequisitionsEntity> jobRequisitions =
                jobRequisitionsRepository.findAllByIsactiveAndRequisitionStatusIn(1, statuses);

        return jobRequisitionMapper.toDTOs(jobRequisitions);
    }

    public String approveJobPostings(ApproveJobPostingsRequest approveJobPostingsRequest) {
        try {
            approveJobPostingsRequest.setUserId(Long.parseLong(securityUtils.getCurrentUserId()));
            for (UUID jobRequisitionId : approveJobPostingsRequest.getRequisitionIdList()) {
                JobRequisitionsEntity jobRequisitions = jobRequisitionsRepository.findById(jobRequisitionId)
                        .orElseThrow(() -> new RuntimeException("Failed to approve job postings: Requisition with ID " + jobRequisitionId + " not found."));
                JobRequisitionsEntity jobRequisitionsOld = jobRequisitions.toBuilder().build();

                WorkflowApprovalEntity workflowEntity;
                workflowEntity = workflowApprovalEntityRepository.findByEntityTypeAndEntityIdAndApproverId(
                        AppConstants.JOB_REQUISITIONS, jobRequisitions.getRequisitionId(), approveJobPostingsRequest.getUserId()
                );

                if (workflowEntity == null && securityUtils.isAdmin()) {
                    //if workflow entity not found, find the latest workflow entity for admin approval
                    workflowEntity = workflowApprovalEntityRepository.findFirstByEntityTypeAndEntityIdOrderByStepNumberDesc(
                            AppConstants.JOB_REQUISITIONS, jobRequisitions.getRequisitionId());
                        approveJobPostingsRequest.setUserId(workflowEntity.getApproverId());
                    if(workflowEntity == null){
                        return "workflowEntity with Requisition with ID " + jobRequisitions.getRequisitionId() + " not found.";
                    }
                }

                // Deep copy using Lombok builder for audit purpose
                WorkflowApprovalEntity workflowEntityOld = workflowEntity.toBuilder().build();

                //approval flow
                if (approveJobPostingsRequest.getStatus().equals(AppConstants.API_APPROVAL_APPROVED)) {
                    //find n+1 user
                    long userId = approveJobPostingsRequest.getUserId();
                    UserEntity manager = userRepository.findById(userId)
                            .map(UserEntity::getManagerId)
                            .flatMap(userRepository::findById)
                            .orElse(null);

                    //if no of approvals is met or if final level manager then direct approve
                    if (workflowEntity.getStepNumber() == jobRequisitions.getNoOfApprovals() || manager == null) {
                        //update jobrequisition record as approved
                        jobRequisitions.setRequisitionStatus(AppConstants.REQ_APPROVAL_APPROVED);
                        jobRequisitions.setRequisitionComments(approveJobPostingsRequest.getDescription());
                        jobRequisitionsRepository.saveWithAudit(jobRequisitionsOld,jobRequisitions);

                        //update workflow record as approved
                        workflowEntity.setComments(approveJobPostingsRequest.getDescription());
                        workflowEntity.setAction(AppConstants.WORKFLOW_ACTION_APPROVED);
                        workflowEntity.setStatus(AppConstants.WORKFLOW_STATUS_COMPLETED);
                        workflowApprovalEntityRepository.saveWithAudit(workflowEntityOld,workflowEntity);

                        return "Job postings approved successfully";
                    } else if (workflowEntity.getStepNumber() < jobRequisitions.getNoOfApprovals()) {
                        //update jobRequisition record comments
                        jobRequisitions.setRequisitionComments(approveJobPostingsRequest.getDescription());
                        jobRequisitionsRepository.saveWithAudit(jobRequisitionsOld,jobRequisitions);

                        //create next level workflow record
                        createWorkflowApprovalPendingRecord(manager, jobRequisitions.getRequisitionId(), workflowEntity.getStepNumber() + 1);

                        //update current workflow record
                        workflowEntity.setComments(approveJobPostingsRequest.getDescription());
                        workflowEntity.setAction(AppConstants.WORKFLOW_ACTION_APPROVED);
                        workflowApprovalEntityRepository.saveWithAudit(workflowEntityOld,workflowEntity);
                        return "Job postings approval moved to next step";
                    }
                } else if (approveJobPostingsRequest.getStatus().equals(AppConstants.API_APPROVAL_REJECTED)) {
                    //update jobrequisition for denied
                    jobRequisitions.setRequisitionComments(approveJobPostingsRequest.getDescription());
                    jobRequisitions.setRequisitionStatus(AppConstants.REQ_APPROVAL_REJECTED);
                    jobRequisitionsRepository.saveWithAudit(jobRequisitionsOld,jobRequisitions);

                    //update workflow entity for denied
                    workflowEntity.setStatus(AppConstants.WORKFLOW_STATUS_COMPLETED);
                    workflowEntity.setComments(approveJobPostingsRequest.getDescription());
                    workflowEntity.setAction(AppConstants.WORKFLOW_ACTION_REJECTED);
                    workflowApprovalEntityRepository.saveWithAudit(workflowEntityOld,workflowEntity);
                    return "Job postings rejected";
                }
            }
        } catch (Exception e) {
            return "Failed to approve job postings: " + e.getMessage();
        }
        return "";
    }

    private void createWorkflowApprovalPendingRecord(UserEntity manager, UUID reqId, int stepNo) {
        WorkflowApprovalEntity workflowApprovalEntity = WorkflowApprovalEntity.builder()
            .entityType(AppConstants.JOB_REQUISITIONS)
            .entityId(reqId)
            .stepNumber(stepNo)
            .approverRole(manager.getRole())
            .approverId(manager.getUserId())
            .action(AppConstants.WORKFLOW_ACTION_PENDING)
            .actionDate(LocalDateTime.now())
            .comments(AppConstants.WORKFLOW_COMMENTS_PENDING)
            .status(AppConstants.WORKFLOW_STATUS_PENDING)
            .build();
        workflowApprovalEntityRepository.saveWithAudit(null,workflowApprovalEntity);
    }

    public List<JobRequisitionsDTO> getJobPostingsNeedApproval(String role) {
        if (role.equals("L1")) {
            List<JobRequisitionsEntity> jobRequisitions=jobRequisitionsRepository.findAllByRequisitionStatusAndIsactive("Pending L1 Approval",1);
            return jobRequisitionMapper.toDTOs(jobRequisitions);

        } else if (role.equals("L2")) {
            List<JobRequisitionsEntity> jobRequisitions=jobRequisitionsRepository.findAllByRequisitionStatusAndIsactive("Pending L2 Approval",1);
            return jobRequisitionMapper.toDTOs(jobRequisitions);
        } else {
            return List.of(); // Return an empty list for other roles
        }
    }

    public List<JobRequisitionsDTO> getApprovalsByUserId(Long userId) {
        List<WorkflowApprovalEntity> approvals = getWorkflowApprovalsEntityByUserId(userId);

        // 2. Collect requisitionIds
        List<UUID> requisitionIds = approvals.stream()
                .map(WorkflowApprovalEntity::getEntityId)
                .collect(Collectors.toList());
        if (requisitionIds.isEmpty()) {
            return List.of();
        }
        // 3. Fetch all active job requisitions with those IDs
//        List<JobRequisitionsEntity> jobRequisitions = jobRequisitionsRepository.findAllById(requisitionIds).stream().filter(jobRequisitionsEntity -> jobRequisitionsEntity.getIsactive()==1).toList();

        List<JobRequisitionsEntity> jobRequisitions = jobRequisitionsRepository.findAllByRequisitionIdInAndIsactiveOrderByRequisitionCodeDesc(requisitionIds,1);
        // 4. Map to DTOs and return
        return jobRequisitionMapper.toDTOs(jobRequisitions);
    }

    public List<WorkflowApprovalEntity> getWorkflowApprovalsEntityByUserId(Long userId) {
        //check if user is admin
        if(securityUtils.isAdmin()){
            return workflowApprovalEntityRepository
                    .findByEntityType(AppConstants.JOB_REQUISITIONS);
        }else{
            return workflowApprovalEntityRepository
                    .findByApproverIdAndEntityType(userId,
                            AppConstants.JOB_REQUISITIONS);
        }


    }

    public List<WorkflowApprovalDTO> getWorkflowApprovalsDTOByUserId(Long userId) {
        return WorkflowApprovalMapper.INSTANCE.toDTOs(getWorkflowApprovalsEntityByUserId(userId));
    }

    public List<WorkflowApprovalsDetailsModel> getWorkflowApprovalsDTOByRequisitionId(UUID requisitionId) {
        List<WorkflowApprovalEntity> workflowApprovalEntities = workflowApprovalEntityRepository.findByEntityIdOrderByActionDateDesc(requisitionId);

        if(workflowApprovalEntities.isEmpty()){
            return List.of();
        }
        List<Long> userIds = workflowApprovalEntities.stream().map(WorkflowApprovalEntity::getApproverId).toList();
        List<UserEntity> userEntitys = userRepository.findAllById(userIds);

        List<WorkflowApprovalsDetailsModel> workflowApprovalsDetailsModels = new ArrayList<>();

        for(WorkflowApprovalEntity workflowApprovalEntity : workflowApprovalEntities){
            WorkflowApprovalsDetailsModel workflowApprovalsDetailsModel = new WorkflowApprovalsDetailsModel();
            workflowApprovalsDetailsModel.setStatus(workflowApprovalEntity.getAction());
            workflowApprovalsDetailsModel.setDateTime(workflowApprovalEntity.getActionDate());
            UserEntity userEntity = userEntitys.stream()
                    .filter(user -> user.getUserId().equals(workflowApprovalEntity.getApproverId()))
                    .findFirst()
                    .orElse(null);
            workflowApprovalsDetailsModel.setComments(workflowApprovalEntity.getComments());
            workflowApprovalsDetailsModel.setMail(userEntity != null ? userEntity.getEmail() : "N/A");
            workflowApprovalsDetailsModel.setUserName(userEntity != null ? userEntity.getName()  : "N/A");
            workflowApprovalsDetailsModels.add(workflowApprovalsDetailsModel);

        }
        return workflowApprovalsDetailsModels;
    }
}

