package com.bob.db.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "job_application_fee", schema = "public")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobApplicationFeeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "job_application_fee_id", nullable = false)
    private Integer jobApplicationFeeId;

    @Column(name = "position_id", nullable = false)
    private UUID positionId;

    @Column(name = "requisition_id", nullable = false)
    private UUID requisitionId;

    @Column(name = "application_fee_id", nullable = false)
    private Integer applicationFeeId;

}
