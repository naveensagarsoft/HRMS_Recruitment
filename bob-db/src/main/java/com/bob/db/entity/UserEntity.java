package com.bob.db.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "users", schema = "public")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@EntityListeners(AuditingEntityListener.class)
public class UserEntity {
    @Id
    @Column(name = "userid")
    private Long userId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "role")
    private String role;

    @Column(name = "email")
    private String email;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "created_by")
    @CreatedBy
    private String createdBy;

    @Column(name = "updated_date")
    private LocalDateTime updatedDate;
    @LastModifiedBy
    @Column(name = "updated_by")

    private String updatedBy;

    @Column(name = "manager_id")
    private Long managerId;

    @Column(name = "step_level")
    private Integer stepLevel;

    @Column(name = "user_password")
    private String userPassword;

    @Column(name = "is_active")
    private Boolean isActive=true;

    @Column(name = "oath_user_id")
    private String oathUserId;
}