package com.bob.db.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LocationDTO {

    @JsonProperty("location_id")
    private Long locationId;

    @JsonProperty("location_name")
    private String locationName;

    @JsonProperty("city_id")
    private Long cityId;

    @JsonIgnore
    @JsonProperty("created_date")
    private LocalDateTime createdDate;

    @JsonIgnore
    @JsonProperty("created_by")
    private String createdBy;


}
