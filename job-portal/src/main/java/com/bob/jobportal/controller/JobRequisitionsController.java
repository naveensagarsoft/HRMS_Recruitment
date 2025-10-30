package com.bob.jobportal.controller;

import com.bob.db.dto.ApiResponse;
import com.bob.db.dto.JobRequisitionsDTO;
import com.bob.db.dto.WorkflowApprovalDTO;
import com.bob.jobportal.model.ApproveJobPostingsRequest;
import com.bob.jobportal.model.JobPostingSubmissionRequest;
import com.bob.jobportal.model.JobRequisitionModel;
import com.bob.jobportal.model.WorkflowApprovalsDetailsModel;
import com.bob.jobportal.service.JobRequisitionsService;
import jdk.jfr.Description;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/job-requisitions")
public class JobRequisitionsController {
    @Autowired
    JobRequisitionsService jobRequisitionsService;


    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<JobRequisitionModel>>> getAll() {
        List<JobRequisitionModel> jobRequisitionsList = jobRequisitionsService.getAll();

        if (jobRequisitionsList.isEmpty()) {
            ApiResponse<List<JobRequisitionModel>> apiResponse = new ApiResponse<>(
                    false,
                    "No job requisitions found",
                    jobRequisitionsList
            );
            return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
        }

        ApiResponse<List<JobRequisitionModel>> apiResponse = new ApiResponse<>(
                true,
                "Active jobs found",
                jobRequisitionsList
        );
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/get-by-status/{requisitionStatus}")
    @Description("Get job requisitions by status")
    public ResponseEntity<ApiResponse<List<JobRequisitionsDTO>>> getByStatus(@PathVariable String requisitionStatus) {

        List<JobRequisitionsDTO> jobRequisitionsList = jobRequisitionsService.findByRequisitionStatus(requisitionStatus);

        if (jobRequisitionsList.isEmpty()) {
            ApiResponse<List<JobRequisitionsDTO>> apiResponse = new ApiResponse<>(
                    false,
                    "No job requisitions found with status: " +  requisitionStatus,
                    jobRequisitionsList
            );
            return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
        }

        ApiResponse<List<JobRequisitionsDTO>> apiResponse = new ApiResponse<>(
                true,
                "Jobs with status: " + requisitionStatus,
                jobRequisitionsList
        );
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
    @GetMapping("/me")
    public String getProfile(@AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getClaim("sub");  // Auth0 user id
        String email = jwt.getClaim("email"); // If Auth0 includes email
        return "User ID: " + userId + ", Email: " + email;
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<JobRequisitionsDTO>> createRequisitions(@RequestBody JobRequisitionsDTO jobRequisitions){
        if (checkFields(jobRequisitions)) {
            throw new IllegalArgumentException("All fields are required");
        }
        JobRequisitionsDTO jobRequisitionsres = jobRequisitionsService.createRequisitions(jobRequisitions);
        ApiResponse<JobRequisitionsDTO> apiResponse =new ApiResponse<>(true,"Created Successful", jobRequisitionsres);
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @PostMapping("/create-bulk")
    public ResponseEntity<?> createBulkRequistions(@RequestBody List<JobRequisitionsDTO> requisitionsList){
        if (requisitionsList == null || requisitionsList.isEmpty()) {
            throw new IllegalArgumentException("Requisitions list cannot be empty");
        }
        for (JobRequisitionsDTO jobRequisitions : requisitionsList) {
            if (checkFields(jobRequisitions)) {
                throw new IllegalArgumentException("All fields are required for each requisition");
            }
        }
        List<JobRequisitionsDTO> createdRequisitions = jobRequisitionsService.createBulkRequistions(requisitionsList);
        ApiResponse<List<JobRequisitionsDTO>> apiResponse = new ApiResponse<>(true, "Bulk Requisitions Created Successfully", createdRequisitions);
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<JobRequisitionsDTO>> updateRequisitions(@RequestBody JobRequisitionsDTO jobRequisitions, @PathVariable UUID id){
        if (checkFields(jobRequisitions)) {
            throw new IllegalArgumentException("All fields are required");
        }
        JobRequisitionsDTO updatedRequisitions = jobRequisitionsService.updateRequisitions(jobRequisitions);
        ApiResponse<JobRequisitionsDTO> apiResponse = new ApiResponse<>(true, "Updated Successfully", updatedRequisitions);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/active")
    public ResponseEntity<ApiResponse<?>>  getActiveRequisitions() {
        List<JobRequisitionsDTO> activeJobs = jobRequisitionsService.getActiveRequisitions();
        if (activeJobs.isEmpty()) {
            ApiResponse<List<JobRequisitionsDTO>> apiResponse = new ApiResponse<>(
                    false,
                    "No active jobs found",
                    activeJobs
            );
            return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
        }

        ApiResponse<List<JobRequisitionsDTO>> apiResponse = new ApiResponse<>(
                true,
                "Active jobs found",
                activeJobs
        );
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<?>> deleteRequisitions(@PathVariable UUID id) {
        String res = jobRequisitionsService.deleteRequisitions(id);
        ApiResponse<JobRequisitionsDTO> apiResponse =new ApiResponse<>(true,"Delete Successful",null);
        return  new ResponseEntity<>(apiResponse,HttpStatus.OK);
    }


    private boolean checkFields(JobRequisitionsDTO jobRequisitions) {
        return jobRequisitions.getRequisitionTitle() == null || jobRequisitions.getRequisitionTitle().trim().isEmpty()
                || jobRequisitions.getRequisitionDescription() == null || jobRequisitions.getRequisitionDescription().trim().isEmpty()
                || jobRequisitions.getRegistrationStartDate() == null
                || jobRequisitions.getRegistrationEndDate() == null
                || jobRequisitions.getNoOfPositions() <= 0;
    }

    //job posting - direct or workflow based submission of job requisition from New status
    @PostMapping("/submit-for-approval")
    public ResponseEntity<ApiResponse<?>> jobPostingSubmission(@RequestBody JobPostingSubmissionRequest jobPostingSubmissionRequest) throws Exception {
        if (jobPostingSubmissionRequest == null
                || jobPostingSubmissionRequest.getRequisitionIds() == null
                || jobPostingSubmissionRequest.getRequisitionIds().isEmpty()) {
            throw new IllegalArgumentException("Requisition Id's cannot be empty");
        }
        String response = jobRequisitionsService.createJobPostings(jobPostingSubmissionRequest);
        ApiResponse<String> apiResponse = new ApiResponse<>(true, "Job postings created successfully", response);
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @PostMapping("/approve")
    public ResponseEntity<ApiResponse<?>> approveJobPostings(@RequestBody ApproveJobPostingsRequest request) {
        if (request == null
                || request.getRequisitionIdList() == null
                || request.getRequisitionIdList().isEmpty()
                || request.getStatus() == null
                || request.getStatus().trim().isEmpty()
                || request.getUserId() == null
                || request.getUserId() == 0) {
            throw new IllegalArgumentException("All fields are required");
        }

        String result = jobRequisitionsService.approveJobPostings(request);
        ApiResponse<String> apiResponse = new ApiResponse<>(true, result, "Succes");
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/need-approval/{role}")
    public ResponseEntity<ApiResponse<List<JobRequisitionsDTO>>> getJobPostingsNeedApproval(@PathVariable String role) {
        List<JobRequisitionsDTO> jobRequisitionsList = jobRequisitionsService.getJobPostingsNeedApproval(role);
        if (jobRequisitionsList.isEmpty()) {
            ApiResponse<List<JobRequisitionsDTO>> apiResponse = new ApiResponse<>(
                    false,
                    "No job postings found that need approval for role: " + role,
                    jobRequisitionsList
            );
            return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
        }
        ApiResponse<List<JobRequisitionsDTO>> apiResponse = new ApiResponse<>(
                true,
                "Job postings found that need approval for role: " + role,
                jobRequisitionsList
        );
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/approvals/{userId}")
    public ResponseEntity<ApiResponse<List<JobRequisitionsDTO>>> getApprovalsForUser(@PathVariable Long userId) {
        List<JobRequisitionsDTO> approvals = jobRequisitionsService.getApprovalsByUserId(userId);
        ApiResponse<List<JobRequisitionsDTO>> apiResponse = new ApiResponse<>(
                true,
                "Approvals fetched successfully",
                approvals
        );
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/workflow-approvals/{userId}")
    public ResponseEntity<ApiResponse<List<WorkflowApprovalDTO>>> getWorkflowApprovalsByUserId(@PathVariable Long userId) {
        List<WorkflowApprovalDTO> dtos = jobRequisitionsService.getWorkflowApprovalsDTOByUserId(userId);
        ApiResponse<List<WorkflowApprovalDTO>> apiResponse = new ApiResponse<>(
                true,
                dtos.isEmpty() ? "No workflow approvals found" : "Workflow approvals fetched successfully",
                dtos
        );
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/workflow-approvals-details/{requisitionId}")
    public ResponseEntity<ApiResponse<List<WorkflowApprovalsDetailsModel>>> getWorkflowApprovalsByRequisitionId(@PathVariable UUID requisitionId) {
        List<WorkflowApprovalsDetailsModel> dtos = jobRequisitionsService.getWorkflowApprovalsDTOByRequisitionId(requisitionId);
        ApiResponse<List<WorkflowApprovalsDetailsModel>> apiResponse = new ApiResponse<>(
                true,
                dtos.isEmpty() ? "No workflow approvals found" : "Workflow approvals fetched successfully",
                dtos
        );
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

}