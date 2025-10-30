package com.bob.candidateportal.model;

import java.util.List;
import java.util.UUID;

public class BulkShortlistRequest {
    private UUID positionId;
    private List<UUID> candidateIds;

    public UUID getPositionId() {
        return positionId;
    }

    public void setPositionId(UUID positionId) {
        this.positionId = positionId;
    }

    public List<UUID> getCandidateIds() {
        return candidateIds;
    }

    public void setCandidateIds(List<UUID> candidateIds) {
        this.candidateIds = candidateIds;
    }
}

