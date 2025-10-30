package com.bob.db.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class CandidatesDTO {
    @JsonProperty("candidate_id")
    private UUID candidateId;

    @JsonProperty("full_name")
    private String fullName;

    @JsonProperty("username")
    private String username;

    @JsonProperty("password_hash")
    private String passwordHash;

    @JsonProperty("email")
    private String email;

    @JsonProperty("phone")
    private String phone;

    @JsonProperty("date_of_birth")
    private LocalDate dateOfBirth;

    @JsonProperty("gender")
    private String gender;

    @JsonProperty("id_proof")
    private String idProof;

    @JsonProperty("nationality_id")
    private Integer nationalityId;

    @JsonProperty("reservation_category_id")
    private Integer reservationCategoryId;

    @JsonProperty("special_category_id")
    private Integer specialCategoryId;

    @JsonProperty("highest_qualification_id")
    private Integer highestQualificationId;

    @JsonProperty("total_experience")
    private String totalExperience;

    @JsonProperty("address")
    private String address;

    @JsonProperty("comments")
    private String comments;

    @JsonProperty("created_date")
    private LocalDateTime createdDate;

    @JsonProperty("skills")
    private String skills;

    @JsonProperty("current_designation")
    private String currentDesignation;

    @JsonProperty("current_employer")
    private String currentEmployer;

    @JsonProperty("file_url")
    private String fileUrl;

    @JsonProperty("education_qualification")
    private String educationQualification;

    @JsonProperty("document_url")
    private String documentUrl;

    private Integer rank;

    @JsonProperty("is_dob_validated")
    private boolean isDobValidated;
}
