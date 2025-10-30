package com.bob.db.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Data
public class InterviewsDTO {

    @JsonProperty("interview_id")
    private UUID interviewId;

    @JsonProperty("application_id")
    private UUID applicationId;

    @JsonProperty("date")
    private LocalDate date;

    @JsonProperty("time")
    private LocalTime time;

//    @JsonProperty("position_id")
//    private UUID positionId;

    @JsonProperty("status")
    private String status;

    @JsonProperty("type")
    private String type;

    @JsonProperty("is_panel_interview")
    private Boolean isPanelInterview;

    @JsonProperty("phone")
    private String phone;

    @JsonProperty("location")
    private String location;

}
