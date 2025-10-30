package com.bob.db.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class WorkflowApprovalDTO {
    private UUID approvalId;
    private String entityType;
    private UUID entityId;
    private Integer stepNumber;
    private String approverRole;
    private Long approverId;
    private String action;
    private LocalDateTime actionDate;
    private String comments;
    private String status;
}

