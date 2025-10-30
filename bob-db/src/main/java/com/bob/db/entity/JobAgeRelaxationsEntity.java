package com.bob.db.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "job_age_relaxations", schema = "public")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobAgeRelaxationsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "job_relaxation_id", nullable = false)
    private Integer jobRelaxationId;

    @Column(name = "position_id")
    private UUID positionId;

    @Column(name = "age_relaxation_id", nullable = false)
    private Integer ageRelaxationId;

}
