package com.bob.db.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CityDTO {

    @JsonProperty("city_id")
    private Long cityId;

    @JsonProperty("city_name")
    private String cityName;

    @JsonProperty("state_id")
    private Long stateId;

    @JsonIgnore
    @JsonProperty("created_date")
    private LocalDateTime createdDate;

    @JsonIgnore
    @JsonProperty("created_by")
    private String createdBy;

}
