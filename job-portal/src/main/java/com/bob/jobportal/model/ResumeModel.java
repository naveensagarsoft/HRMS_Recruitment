package com.bob.jobportal.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResumeModel {
    private String name;
    private String email;
    private String address;
    private String mobile_raw;
    private String mobile_intl_std;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate dateOfBirth;
    private String gender;
    private List<ResumeEducationModel> educations;
    private List<ResumeExperienceModel> experiences;
}
