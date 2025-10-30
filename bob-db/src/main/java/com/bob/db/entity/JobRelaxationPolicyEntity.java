package com.bob.db.entity;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "job_relaxation_policy", schema = "public")
@Data
@EntityListeners(AuditingEntityListener.class)
public class JobRelaxationPolicyEntity {

    @Column(name = "job_relaxation_policy_id", nullable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID jobRelaxationPolicyId;

//    @Column(name = "position_id", nullable = false)
//    private UUID positionId;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "relaxation", columnDefinition = "jsonb", nullable = false)
    private JsonNode relaxation;

    @Column(name = "created_date")
    @CreationTimestamp
    private LocalDateTime createdDate;

    @Column(name = "created_by")
    @CreatedBy
    private String createdBy;

    @Column(name = "updated_date")
    @UpdateTimestamp
    private LocalDateTime updatedDate;

    @Column(name = "updated_by")
    @LastModifiedBy
    private String updatedBy;

    @Column(name="relaxation_policy_number")
    private String relaxationPolicyNumber;
}
