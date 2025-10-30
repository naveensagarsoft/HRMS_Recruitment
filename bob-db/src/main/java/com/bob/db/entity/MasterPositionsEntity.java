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

import java.time.LocalDateTime;

@Entity
@Table(name = "master_positions", schema = "public", uniqueConstraints = {
        @UniqueConstraint(columnNames = "position_code")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class MasterPositionsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "master_position_id")
    private Long masterPositionId;

    @Column(name = "dept_id")
    private Long deptId;

    @Column(name = "position_code", nullable = false, length = 50, unique = true)
    private String positionCode;

    @Column(name = "position_name", nullable = false, length = 255)
    private String positionName;

    @Column(name = "position_description")
    private String positionDescription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_grade_id", referencedColumnName = "job_grade_id", foreignKey = @ForeignKey(name = "master_positions_job_grade_id_fkey"))
    private JobGradeEntity jobGrade;

    @Column(name = "is_active")
    private Boolean isActive=true;

    @Column(name = "created_by", nullable = false, length = 255)
    @CreatedBy
    private String createdBy;

    @Column(name = "created_date")
    @CreationTimestamp
    private LocalDateTime createdDate;

    @Column(name = "updated_by", length = 255)
    @LastModifiedBy
    private String updatedBy;

    @Column(name = "updated_date")
    @UpdateTimestamp
    private LocalDateTime updatedDate;


}
