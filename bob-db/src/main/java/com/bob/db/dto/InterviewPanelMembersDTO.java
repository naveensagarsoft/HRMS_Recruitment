package com.bob.db.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class InterviewPanelMembersDTO {

    @JsonProperty("panel_member_id")
    private Long panelMemberId;

    @JsonProperty("panel_id")
    private Long panelId;

    @JsonProperty("interviewer_id")
    private Long interviewerId;

    @JsonProperty("role_in_panel")
    private String roleInPanel;

    @JsonProperty("is_primary")
    private Boolean isPrimary;

    @JsonProperty("added_date")
    private LocalDateTime addedDate;
}
