package com.bob.candidateportal.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

@Data
public class CandidateDetailsResponse {

    @JsonProperty("candidate_id")
    private UUID candidateId;

    @JsonProperty("full_name")
    private String fullName;

    @JsonProperty("username")
    private String username;

    @JsonProperty("country_details")
    private Map<Long,String> countryDetails;

    @JsonProperty("state_details")
    private Map<Long,String> stateDetails;

    @JsonProperty("location_details")
    private Map<Long,String> locationDetails;

    @JsonProperty("city_details")
    private Map<Long,String> cityDetails;

    @JsonProperty("email")
    private String email;

    @JsonProperty("gender")
    private String gender;

    @JsonProperty("phone")
    private String phone;

    @JsonProperty("reservation_category_id")
    private Integer reservationCategoryId;

    @JsonProperty("special_category_id")
    private Integer specialCategoryId;


    @JsonProperty("total_experience")
    private String totalExperience;

    @JsonProperty("address")
    private String address;

    @JsonProperty("comments")
    private String comments;

    @JsonProperty("application_status")
    private String applicationStatus;

    @JsonProperty("file_url")
    private String fileUrl;

    @JsonProperty("document_url")
    private String documentUrl;

    private String offerLetterUrl;

    @JsonProperty("date_of_birth")
    private LocalDate dateOfBirth;

    @JsonProperty("id_proof")
    private String idProof;

    @JsonProperty("nationality_id")
    private Integer nationalityId;

    @JsonProperty("highest_qualification_id")
    private Integer highestQualificationId;

    private String skills;

    @JsonProperty("current_designation")
    private String currentDesignation;

    @JsonProperty("current_employer")
    private String currentEmployer;

    @JsonProperty("education_qualification")
    private String educationQualification;

    private Integer rank;

}
