package com.bob.db.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

import java.util.UUID;

@Data
public class JobRelaxationPolicyDTO {

    @JsonProperty("job_relaxation_policy_id")
    private UUID jobRelaxationPolicyId;

//    @JsonProperty("position_id")
//    private UUID positionId;

    @JsonProperty("relaxation")
    private JsonNode relaxation;

    @JsonProperty("relaxation_policy_number")
    private String relaxationPolicyNumber;
}
