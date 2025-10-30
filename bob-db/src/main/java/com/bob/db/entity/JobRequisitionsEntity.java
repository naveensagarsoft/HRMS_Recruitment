package com.bob.db.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;


@Entity
@Table(name = "job_requisitions", schema = "public")
@Data
@EntityListeners(AuditingEntityListener.class)
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class JobRequisitionsEntity {

    public static final String ENTITY_TYPE = "job_requisitions";

    @Id
    @Column(name = "requisition_id", updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID requisitionId;
    @Column(name = "requisition_code", unique = true, nullable = false)
    private String requisitionCode;
    @Column(name = "requisition_title", nullable = false)
    private String requisitionTitle;

    @Column(name = "requisition_description", columnDefinition = "text")
    private String requisitionDescription;
    @Column(name = "registration_start_date")
    private LocalDate registrationStartDate;
    @Column(name = "registration_end_date")
    private LocalDate registrationEndDate;
    @Column(name = "requisition_status")
    private String requisitionStatus;

    @Column(name = "requisition_comments", columnDefinition = "text")
    private String requisitionComments;

    @Column(name = "requisition_approval")
    private String requisitionApproval;

    @Column(name = "created_by")
    @CreatedBy
    private String createdBy;


    @Column(name = "created_date", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdDate;

    @Column(name = "updated_by")
    @LastModifiedBy
    private String updatedBy;
    @Column(name = "updated_date")
    @UpdateTimestamp
    private LocalDateTime updatedDate;

    @Column(name = "others" , columnDefinition = "jsonb")
    private String others;
    @Column(name = "no_of_positions")
    private Integer noOfPositions;

    @Column(name = "job_postings", columnDefinition = "text")
    private String jobPostings;

    @Column(name = "requisition_approval_notes", columnDefinition = "text")
    private String requisitionApprovalNotes;
    @Column(name = "isactive")
    private Integer isactive = 1;

    @Column(name="no_of_approvals")
    private Integer noOfApprovals = 1;

}
