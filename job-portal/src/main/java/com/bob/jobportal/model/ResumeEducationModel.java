package com.bob.jobportal.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResumeEducationModel {
    private String degree;
    private String endYear;
    private String educationOrg;

}

