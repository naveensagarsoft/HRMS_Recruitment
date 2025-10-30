package com.bob.db.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class OfferDTO {

    @JsonProperty("candidate_id")
    private UUID candidateId;

    @JsonProperty("position_id")
    private UUID positionId;

    @JsonProperty("salary")
    private BigDecimal salary;

    @JsonProperty("designation")
    private String designation;

    @JsonProperty("joining_date")
    private LocalDate joiningDate;

    @JsonProperty("offer_letter_path")
    private String offerLetterPath;

}
