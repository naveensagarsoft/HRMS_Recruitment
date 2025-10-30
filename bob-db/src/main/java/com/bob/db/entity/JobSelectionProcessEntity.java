package com.bob.db.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "job_selection_process", schema = "public")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobSelectionProcessEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "job_selection_id", updatable = false, nullable = false)
    private Integer jobSelectionId;
    @Column(name = "position_id", nullable = false)
    private UUID positionId;
    @Column(name = "selection_procedure", columnDefinition = "text")
    private String selectionProcedure;
}