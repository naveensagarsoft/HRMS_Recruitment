package com.bob.db.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
@Data
public class JobGradeDTO {
    @JsonProperty("job_grade_id")
    private Long jobGradeId;

    @JsonProperty("job_grade_code")
    private String jobGradeCode;

    @JsonProperty("job_grade_desc")
    private String jobGradeDesc;

    @JsonProperty("job_scale")
    private String jobScale;

    @JsonProperty("min_salary")
    private BigDecimal minSalary;

    @JsonProperty("max_salary")
    private BigDecimal maxSalary;

    @JsonProperty("effective_state_date")
    private LocalDate effectiveStateDate;

    @JsonProperty("effective_end_date")
    private LocalDate effectiveEndDate;

    @JsonProperty("created_by")
    private String createdBy;
}
