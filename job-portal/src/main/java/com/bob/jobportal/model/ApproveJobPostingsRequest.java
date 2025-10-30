package com.bob.jobportal.model;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class ApproveJobPostingsRequest {

    private List<UUID> requisitionIdList;
    private String status;
    private String description;
    private Long userId;

}
