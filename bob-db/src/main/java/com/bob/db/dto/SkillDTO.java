package com.bob.db.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;
@Data
public class SkillDTO {

    @JsonProperty("skill_id")
    private Long skillId;

    @JsonProperty("skill_name")
    private String skillName;

    @JsonProperty("skill_desc")
    private String skillDesc;

    @JsonProperty("start_date")
    private LocalDate startDate;

    @JsonProperty("end_date")
    private LocalDate endDate;

}
