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

@Entity
@Table(name = "relaxation_types", schema = "public")
@Data
@EntityListeners(AuditingEntityListener.class)
public class RelaxationTypesEntity {
    @Column(name = "relaxation_type_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer relaxationTypeId;

    @Column(name = "relaxation_type_name")
    private String relaxationTypeName;

    @Column(name = "description")
    private String description;


    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "others", columnDefinition = "jsonb")
    private JsonNode others;

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

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "input")
    private String input;

    @Column(name = "operator")
    private String operator;

}
