package com.bob.jobportal.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class JobPostingSubmissionRequest {
    @JsonProperty("requisition_id")
    private List<UUID> requisitionIds;

    @JsonProperty("job_postings")
    private List<String> jobPostings;

    @JsonProperty("approval_status")
    private String approvalStatus;

    @JsonProperty("userId")
    private long userId;

    @JsonProperty("noOfApprovals")
    private int noOfApprovals;
}
