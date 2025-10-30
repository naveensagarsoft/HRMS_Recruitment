package com.bob.db.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "special_categories", schema = "public")
@Data
@EntityListeners(AuditingEntityListener.class)
public class SpecialCategoriesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "special_category_id", nullable = false)
    private Long specialCategoryId;

    @Column(name = "special_category_code",nullable = false)
    private String specialCategoryCode;

    @Column(name = "special_category_name")
    private String specialCategoryName;

    @Column(name = "special_category_desc", columnDefinition = "text")
    private String specialCategoryDesc;

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

}
