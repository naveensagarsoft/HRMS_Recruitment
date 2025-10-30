package com.bob.db.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "templates", schema = "public")
@Data
@EntityListeners(AuditingEntityListener.class)
public class TemplatesEntity {
    @Id
    @Column(name = "template_id")
    private Integer templateId;

    @Column(name = "template_type")
    private String templateType;

    @Column(name = "template_name")
    private String templateName;

    @Column(name = "template_desc", columnDefinition = "text")
    private String templateDesc;

    @Column(name = "file_path", columnDefinition = "text")
    private String filePath;

    @Column(name = "others", columnDefinition = "jsonb")
    private String others;

    @Column(name = "created_by")
    @CreatedBy
    private String createdBy;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "updated_by")
    @LastModifiedBy
    private String updatedBy;

    @Column(name = "updated_date")
    private LocalDateTime updatedDate;


}
