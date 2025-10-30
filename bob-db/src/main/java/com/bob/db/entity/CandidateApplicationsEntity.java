package com.bob.db.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "candidate_applications", schema = "public")
@Data
@EntityListeners(AuditingEntityListener.class)
public class CandidateApplicationsEntity {

    public static final String ENTITY_TYPE = "candidate_applications";

    @Id
    @Column(name = "application_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID applicationId;

    @OneToOne
    @JoinColumn(name = "candidate_id", referencedColumnName = "candidate_id", insertable = false, updatable = false)
    private CandidatesEntity candidate;

    @OneToOne
    @JoinColumn(name = "position_id", referencedColumnName = "position_id", insertable = false, updatable = false)
    private PositionsEntity position;

    @Column(name = "position_id", nullable = false)
    private UUID positionId;

    @Column(name = "candidate_id", nullable = false)
    private UUID candidateId;

    @Column(name = "application_status")
    private String  applicationStatus;

    @Column(name = "application_date")
    private LocalDateTime applicationDate;

    @Column(name = "ctc",precision = 15, scale = 2)
    private BigDecimal ctc;

    @Column(name = "designation")
    private String designation;

    @Column(name = "joining_date")
    private LocalDate joiningDate;

    @Column(name = "updated_date")
    @UpdateTimestamp
    private LocalDateTime updatedDate;

    @Column(name = "updated_by")
    @LastModifiedBy
    private String updatedBy;

    @Column(name = "created_date")
    @CreationTimestamp
    private LocalDateTime createdDate;

    @Column(name = "created_by")
    @CreatedBy
    private String createdBy;

    public enum ApplicationStatus {
        RESCHEDULED("Rescheduled"),
        SCHEDULED("Scheduled"),
        SELECTED_FOR_NEXT_ROUND("Selected for next round"),
        NOT_AVAILABLE("Not available"),
        SELECTED("Selected"),
        REJECTED("Rejected"),
        CANCELLED("Cancelled"),
        SHORTLISTED("Shortlisted"),
        OFFERED("Offered");

        private final String displayName;

        ApplicationStatus(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
}
