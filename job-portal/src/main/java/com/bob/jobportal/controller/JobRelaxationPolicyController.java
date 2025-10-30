package com.bob.jobportal.controller;

import com.bob.db.dto.ApiResponse;
import com.bob.db.dto.JobRelaxationPolicyDTO;
import com.bob.jobportal.service.JobRelaxationPolicyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/job-relaxation-policy")
public class JobRelaxationPolicyController {

    @Autowired
    private JobRelaxationPolicyService jobRelaxationPolicyService;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<?>> addRelaxationPolicy(@RequestBody JobRelaxationPolicyDTO dto) {
        JobRelaxationPolicyDTO jobRelaxationPolicyDTO = jobRelaxationPolicyService.addRelaxationPolicy(dto);
        ApiResponse<JobRelaxationPolicyDTO> apiResponse = new ApiResponse<>(true, "Relaxation policy "+jobRelaxationPolicyDTO.getRelaxationPolicyNumber()+" successfully", jobRelaxationPolicyDTO );
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<?>> getAllRelaxationPolicies() {
        // Implementation for fetching all relaxation policies
        List<JobRelaxationPolicyDTO> policies = jobRelaxationPolicyService.getAllRelaxationPolicies();
        ApiResponse<List<JobRelaxationPolicyDTO>> apiResponse = new ApiResponse<>(true, "Fetched all relaxation policies", policies);
        return ResponseEntity.ok(apiResponse);
    }
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> getRelaxationPolicyById(@PathVariable UUID id) {
        // Implementation for fetching a relaxation policy by ID
        JobRelaxationPolicyDTO policyDTO = jobRelaxationPolicyService.getRelaxationPolicyById(id);
        ApiResponse<JobRelaxationPolicyDTO> apiResponse = new ApiResponse<>(true, "Fetched relaxation policy", policyDTO);
        return ResponseEntity.ok(apiResponse);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<?>> updateRelaxationPolicy(@PathVariable UUID id, @RequestBody JobRelaxationPolicyDTO policy) {
        // Implementation for updating a relaxation policy
        JobRelaxationPolicyDTO policyDTO = jobRelaxationPolicyService.updateRelaxationPolicy(id, policy);
        ApiResponse<JobRelaxationPolicyDTO> apiResponse = new ApiResponse<>(true, "Relaxation policy updated successfully", policyDTO);
            return ResponseEntity.ok(apiResponse);
    }

}
