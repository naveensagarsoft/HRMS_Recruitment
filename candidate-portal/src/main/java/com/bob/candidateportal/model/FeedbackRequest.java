package com.bob.candidateportal.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.UUID;

@Data
public class FeedbackRequest {

    @JsonProperty("application_id")
    private UUID applicationId;
    @JsonProperty("interviewer_id")
    private Long interviewerId;
    @JsonProperty("interviewer_name")
    private String interviewerName;
    @JsonProperty("interviewer_email")
    private String interviewerEmail;
    private String comments;
    private String status;
}
