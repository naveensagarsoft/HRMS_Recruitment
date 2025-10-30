package com.bob.db.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class AuditTrailDTO {
    private UUID auditId;
    private String entityType;
    private UUID entityId;
    private String fieldChanged;
    private String oldValue;
    private String newValue;
    private Long changedBy;
    private LocalDateTime changeDate;
    private String changeType;
}
