package com.bob.db.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
@Data
public class EducationalQualificationsDTO {

    @JsonProperty("edu_qualification_id")
    private Long eduQualificationId;

    @JsonProperty("edu_qualification_name")
    private String eduQualificationName;

    @JsonProperty("edu_desc")
    private String eduDesc;

    @JsonProperty("created_by")
    private String createdBy;

    @JsonProperty("updated_by")
    private String updatedBy;
}
