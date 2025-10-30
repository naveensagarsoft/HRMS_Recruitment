package com.bob.db.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "candidates", schema = "public")
@Data
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CandidatesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "candidate_id", nullable = false, columnDefinition = "uuid")
    private UUID candidateId;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(nullable = false)
    private String username;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(nullable = false)
    private String email;

    @Column
    private String phone;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column
    private String gender;

    @Column(name = "id_proof")
    private String idProof;

    @Column(name = "nationality_id")
    private Integer nationalityId;

    @Column(name = "reservation_category_id")
    private Integer reservationCategoryId;

    @Column(name = "special_category_id")
    private Integer specialCategoryId;

    @Column(name = "highest_qualification_id")
    private Integer highestQualificationId;

    @Column(name = "total_experience", columnDefinition = "text")
    private String totalExperience;

    @Column(columnDefinition = "text")
    private String address;

    @Column(name = "comments", columnDefinition = "text")
    private String comments;

    @Column(name = "created_date")
    @CreationTimestamp
    private LocalDateTime createdDate;

    @Column(columnDefinition = "text")
    private String skills;

    @Column(name = "current_designation")
    private String currentDesignation;

    @Column(name = "current_employer")
    private String currentEmployer;

    @Column(name = "education_qualification")
    private String educationQualification;

    @Column(name = "file_url", columnDefinition = "text")
    private String fileUrl;

    @Column(name = "document_url", columnDefinition = "text")
    private String documentUrl;

    @Column(name = "rank")
    private Integer rank;

    @Column(name = "created_by", length = 255)
    @CreatedBy
    private String createdBy;

    @Column(name = "updated_by", length = 255)
    @LastModifiedBy
    private String updatedBy;

    @Column(name = "is_active")
    private boolean isActive =true;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "candidate_other_details", columnDefinition = "jsonb")
    private JsonNode candidateOtherDetails;

    @Column(name = "is_bulk_upload", nullable = true)
    private boolean isBulkUpload = false;

    @Column(name="is_dob_validated")
    private boolean isDobValidated;
}
