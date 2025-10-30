package com.bob.db.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "positions", schema = "public")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class PositionsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "position_id", nullable = false, columnDefinition = "uuid")
    private UUID positionId;

    @Column(name = "requisition_id")
    private UUID requisitionId;

    @Column(name = "position_title", nullable = false)
    private String positionTitle;

    @Column(columnDefinition = "text")
    private String description;

    @Column(name = "position_code")
    private String positionCode;

    @Column(name = "roles_responsibilities", columnDefinition = "text")
    private String rolesResponsibilities;

    @Column(name = "grade_id")
    private Integer gradeId;

    @Column(name = "employment_type")
    private String employmentType;

    @Column(name = "eligibility_age_min")
    private Integer eligibilityAgeMin;

    @Column(name = "eligibility_age_max")
    private Integer eligibilityAgeMax;

    @Column(name = "mandatory_qualification", columnDefinition = "text")
    private String mandatoryQualification;

    @Column(name = "preferred_qualification", columnDefinition = "text")
    private String preferredQualification;

    @Column(name = "mandatory_experience")
    private BigDecimal mandatoryExperience;

    @Column(name = "preferred_experience")
    private BigDecimal preferredExperience;

    @Column(name = "probation_period")
    private BigDecimal probationPeriod;

    @Column(name = "documents_required", columnDefinition = "text")
    private String documentsRequired;

    @Column(name = "min_credit_score")
    private BigDecimal minCreditScore;

    @Column(name = "position_status")
    private String positionStatus;

    @Column(name = "created_by")
    @CreatedBy
    private String createdBy;

    @Column(name = "created_date")
    @CreationTimestamp
    private LocalDateTime createdDate;

    @Column(name = "updated_by")
    @LastModifiedBy
    private String updatedBy;

    @Column(name = "updated_date")
    @UpdateTimestamp
    private LocalDateTime updatedDate;

    @Column(name = "min_salary")
    private BigDecimal minSalary;

    @Column(name = "max_salary")
    private BigDecimal maxSalary;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "job_relaxation_policy_id")
    private UUID jobRelaxationPolicyId;
}
