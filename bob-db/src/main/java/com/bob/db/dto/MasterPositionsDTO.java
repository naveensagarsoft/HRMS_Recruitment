package com.bob.db.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MasterPositionsDTO {
    private Long masterPositionId;
    private String positionCode;
    private String positionName;
    private String positionDescription;
    private Long jobGradeId;
//    private Boolean isActive; //Here IsActive field
    private String createdBy;
    private LocalDateTime createdDate;
    private String updatedBy;
    private LocalDateTime updatedDate;
    private Long deptId;
}

