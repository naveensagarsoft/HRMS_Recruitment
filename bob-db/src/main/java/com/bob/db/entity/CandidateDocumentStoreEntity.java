package com.bob.db.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.LastModifiedBy;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "candidate_document_store", schema = "public")
public class CandidateDocumentStoreEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="document_store_id")
    private UUID documentStoreId;

    @Column(name = "candidate_id")
    private UUID candidateId;

    @Column(name = "document_id")
    private Long documentId;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_url")
    private String fileUrl;

    @Column(name = "uploaded_date")
    @UpdateTimestamp
    private LocalDateTime uploadedDate;
}
