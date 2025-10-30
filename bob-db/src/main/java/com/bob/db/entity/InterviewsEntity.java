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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "interviews", schema = "public")
@EntityListeners(AuditingEntityListener.class)
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class InterviewsEntity {

    public static final String ENTITY_TYPE = "interviews";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "interview_id")
    private UUID interviewId;

    @Column(name = "is_panel_interview")
    private Boolean isPanelInterview;

    @Column(name = "phone")
    private String phone;

    @Column(name="location")
    private String location;

    @Column(name = "application_id")
    private UUID applicationId;

    @Column(name = "type")
    private String type;

    @Column(name = "scheduled_at")
    private LocalDateTime scheduledAt;

    @Column(name = "time", precision = 4, scale = 2)
    private BigDecimal time;

    @Column(name = "status")
    private String status;

    @Column(name = "interviewer_id")
    private Long interviewerId;

    @Column(name = "created_by")
    @CreatedBy
    private String createdBy;

    @Column(name = "created_date")
    @CreationTimestamp
    private LocalDateTime createdDate;

    @Column(name = "updated_by")
    @LastModifiedBy
    private String updatedBy;

    @Column(name = "updated_date")
    @UpdateTimestamp
    private LocalDateTime updatedDate;


}
