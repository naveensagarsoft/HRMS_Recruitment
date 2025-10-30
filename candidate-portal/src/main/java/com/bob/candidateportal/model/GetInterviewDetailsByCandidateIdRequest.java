package com.bob.candidateportal.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.UUID;

@Data
public class GetInterviewDetailsByCandidateIdRequest {

    @JsonProperty("application_id")
    private UUID applicationId;

//    @JsonProperty("position_id")
//    private UUID positionId;

}
