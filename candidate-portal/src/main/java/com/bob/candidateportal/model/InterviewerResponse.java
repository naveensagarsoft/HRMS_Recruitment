package com.bob.candidateportal.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class InterviewerResponse {
    @JsonProperty("interview_id")
    private UUID interviewId;

    @JsonProperty("scheduled_at")
    private LocalDateTime scheduledAt;

    private BigDecimal time;

    private String status;

    private String interviewer;


    @JsonProperty("interviewer_id")
    private Long interviewerId;

    @JsonProperty("interviewer_email")
    private String interviewerEmail;

    @JsonProperty("interview_type")
    private String type;

    @JsonProperty("is_panel_interview")
    private Boolean isPanelInterview;

    @JsonProperty("phone")
    private String phone;

    @JsonProperty("location")
    private String location;



}
