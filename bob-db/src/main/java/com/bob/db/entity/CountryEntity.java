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
@Table(name = "country", schema = "public")
@Data
@EntityListeners(AuditingEntityListener.class)
public class CountryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "country_id", nullable = false)
    private Long countryId;

    @Column(name = "country_name")
    private String countryName;

    @Column(name="created_date", columnDefinition = "timestamp default now()")
    @CreationTimestamp
    private LocalDateTime createdDate;

    @Column(name="created_by")
    @CreatedBy
    private String createdBy;

    @Column(name="is_active")
    private Boolean isActive = true;

    @Column(name="updated_date")
    @UpdateTimestamp
    private LocalDateTime updatedDate;

    @Column(name="updated_by")
    @LastModifiedBy
    private String updatedBy;

}
