package com.bob.db.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class JobRequisitionsDTO {
    @JsonProperty("requisition_id")
    private UUID requisitionId;
    @JsonProperty("requisition_code")
    private String requisitionCode;
    @JsonProperty("requisition_title")
    private String requisitionTitle;
    @JsonProperty("requisition_description")
    private String requisitionDescription;
    @JsonProperty("registration_start_date")
    private LocalDate registrationStartDate;
    @JsonProperty("registration_end_date")
    private LocalDate registrationEndDate;
    @JsonProperty("requisition_status")
    private String requisitionStatus;
    @JsonProperty("requisition_comments")
    private String requisitionComments;

    @JsonProperty("requisition_approval")
    private String requisitionApproval;

    @JsonProperty("others")
    private String others;
    @JsonProperty("no_of_positions")
    private Integer noOfPositions;


    @JsonProperty("job_postings")
    private String jobPostings;

    @JsonProperty("requisition_approval_notes")
    private String requisitionApprovalNotes;

    private Integer isactive = 1; //Same here too.


    @JsonProperty("no_of_approvals")
    private Integer noOfApprovals = 1;
}
