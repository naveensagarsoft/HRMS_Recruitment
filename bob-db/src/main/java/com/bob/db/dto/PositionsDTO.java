package com.bob.db.dto;

import jakarta.persistence.Column;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class PositionsDTO {
    private UUID positionId;
    private UUID requisitionId;

    private String positionTitle;
    private String description;
    private String positionCode;
    private String rolesResponsibilities;

    private Integer gradeId;
    private String employmentType;

    private Integer eligibilityAgeMin;
    private Integer eligibilityAgeMax;

    private String mandatoryQualification;
    private String preferredQualification;

    private BigDecimal mandatoryExperience;
    private BigDecimal preferredExperience;
    private BigDecimal probationPeriod;

    private String documentsRequired;
    private BigDecimal minCreditScore;

    private String positionStatus;

    private String createdBy;
    private LocalDateTime createdDate;

    private String updatedBy;
    private LocalDateTime updatedDate;
    private BigDecimal minSalary;
    private BigDecimal maxSalary;
    private Boolean isActive;
    private UUID jobRelaxationPolicyId;
}
