package com.bob.jobportal.model;



import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobRequisitionModel {

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

    @JsonProperty("no_of_positions")
    private Integer noOfPositions;

    @JsonProperty("job_postings")
    private String jobPostings;

    @JsonProperty("requisition_approval_notes")
    private String requisitionApprovalNotes;

    @JsonProperty("count")
    private Integer count;

    @JsonProperty("fullfillment")
    private Integer fullfillment;

}
