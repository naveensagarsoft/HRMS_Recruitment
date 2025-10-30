package com.bob.candidateportal.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Data
public class ScheduleInterviewRequest {

    @JsonProperty("application_id")
    private UUID applicationId;

    private LocalDate date;

    private LocalTime time;

    @JsonProperty("interviewer_id")
    private Long interviewerId;

    @JsonProperty("interview_type")
    private String type;

    @JsonProperty("is_panel_interview")
    private Boolean isPanelInterview;

    @JsonProperty("phone")
    private String phone;

    @JsonProperty("location")
    private String location;

    private String status;
}
