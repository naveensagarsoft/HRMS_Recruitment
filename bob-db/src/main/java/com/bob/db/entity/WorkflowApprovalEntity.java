package com.bob.db.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "workflow_approval", schema = "public")
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class WorkflowApprovalEntity {

    public static final String ENTITY_TYPE = "workflow_approval";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "approval_id")
    private UUID approvalId;

    @Column(name = "entity_type")
    private String entityType;

    @Column(name = "entity_id")
    private UUID entityId;

    @Column(name = "step_number")
    private Integer stepNumber;

    @Column(name = "approver_role")
    private String approverRole;

    @Column(name = "approver_id")
    private Long approverId;

    @Column(name = "action")
    private String action;

    @Column(name = "action_date")
    private LocalDateTime actionDate;

    @Column(name = "comments")
    private String comments;

    @Column(name = "status")
    private String status;
}