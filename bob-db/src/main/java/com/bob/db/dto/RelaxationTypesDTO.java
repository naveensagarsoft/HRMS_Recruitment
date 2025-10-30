package com.bob.db.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.Column;
import lombok.Data;

@Data
public class RelaxationTypesDTO {
    @JsonProperty("relaxation_type_id")
    private Integer relaxationTypeId;

    @JsonProperty("relaxation_type_name")
    private String relaxationTypeName;

    @JsonProperty("description")
    private String description;

    @JsonProperty("others")
    private JsonNode others;

    @JsonProperty("input")
    private String input;

    @JsonProperty("operator")
    private String operator;
}
