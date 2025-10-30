package com.bob.candidateportal.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class JobPositionsModel {
    @JsonProperty("position_id")
    private UUID positionId;

    @JsonProperty("requisition_id")
    private UUID requisitionId;

    @JsonProperty("position_title")
    private String positionTitle;


    @JsonProperty("requisition_code")
    private String requisitionCode;

    @JsonProperty("requisition_title")
    private String requisitionTitle;

    @JsonProperty("position_code")
    private String positionCode;

    @JsonProperty("description")
    private String description;

    @JsonProperty("roles_responsibilities")
    private String rolesResponsibilities;

    @JsonProperty("grade_id")
    private Integer gradeId;

    @JsonProperty("employment_type")
    private String employmentType;

    @JsonProperty("eligibility_age_min")
    private Integer eligibilityAgeMin;

    @JsonProperty("eligibility_age_max")
    private Integer eligibilityAgeMax;

    @JsonProperty("mandatory_qualification")
    private String mandatoryQualification;

    @JsonProperty("preferred_qualification")
    private String preferredQualification;

    @JsonProperty("mandatory_experience")
    private BigDecimal mandatoryExperience;

    @JsonProperty("preferred_experience")
    private BigDecimal preferredExperience;

    @JsonProperty("probation_period")
    private BigDecimal probationPeriod;

    @JsonProperty("documents_required")
    private String documentsRequired;

    @JsonProperty("min_credit_score")
    private BigDecimal minCreditScore;

    @JsonProperty("dept_id")
    private Integer deptId;

    @JsonProperty("location_id")
    private Long locationId;

    //Job Vacancies

    @JsonProperty("special_cat_id")
    private Integer specialCatId;

    @JsonProperty("reservation_cat_id")
    private Integer reservationCatId;

    @JsonProperty("no_of_vacancies")
    private Integer noOfVacancies;

    //Job Selection Process table

    @JsonProperty("selection_procedure")
    private String selectionProcedure;

    @JsonProperty("position_status")
    private String positionStatus;

    @JsonProperty("min_salary")
    private BigDecimal minSalary;

    @JsonProperty("max_salary")
    private BigDecimal maxSalary;

    //new fields
    @JsonProperty("grade_name")
    private String gradeName;
    @JsonProperty("dept_name")
    private String deptName;
    @JsonProperty("location_name")
    private String locationName;
}
