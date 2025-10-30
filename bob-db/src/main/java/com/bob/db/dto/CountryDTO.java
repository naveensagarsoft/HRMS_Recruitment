package com.bob.db.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CountryDTO {
    @JsonProperty("country_id")
    private Long countryId;

    @JsonProperty("country_name")
    private String countryName;

    @JsonProperty("created_by")
    private String createdBy;
}
