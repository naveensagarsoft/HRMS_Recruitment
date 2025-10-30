package com.bob.db.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class CandidateApplicationDTO {
    private UUID applicationId;
    private String applicationStatus;
    private LocalDateTime applicationDate;
    private LocalDateTime updatedDate;
    private CandidatesDTO candidate;
    private PositionsDTO position;
}
