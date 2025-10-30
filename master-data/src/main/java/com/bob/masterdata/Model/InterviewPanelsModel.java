package com.bob.masterdata.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class InterviewPanelsModel {
    @JsonProperty("panel_id")
    private Long panelId;

    @JsonProperty("panel_name")
    private String panelName;

    @JsonProperty("panel_description")
    private String panelDescription;

    @JsonProperty("interviewer_ids")
    private List<Long> interviewerIds;

}
