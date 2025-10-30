package com.bob.candidateportal.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InterviewResponse {
    private String interviewTime;
    private String interviewTitle;
    private String candidateName;
    private String candidateSkill;
    private String applicationStatus;
    @JsonProperty("requisition_code")
    private String requisitionCode;
    private String interviewName;

}