package com.bob.db.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import lombok.Data;

@Data
public class StateDTO {

    @JsonProperty("state_id")
    private Long stateId;

    @JsonProperty("state_name")
    private String stateName;

    @JsonProperty("country_id")
    private Long countryId;

    @Column(name="created_by")
    @JsonIgnore
    private String createdBy;
}

