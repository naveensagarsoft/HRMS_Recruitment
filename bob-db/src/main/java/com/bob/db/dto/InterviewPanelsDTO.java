package com.bob.db.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
public class InterviewPanelsDTO {

    @JsonProperty("panel_id")
    private Long panelId;

    @JsonProperty("panel_name")
    private String panelName;

    @JsonProperty("description")
    private String description;
}
