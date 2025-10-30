package com.bob.db.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.UUID;

@Data
public class ShortlistCandidateDTO {

    @JsonProperty("candidate_id")
    private UUID candidateId;

    @JsonProperty("position_id")
    private UUID positionId;

}
