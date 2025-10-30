package com.bob.db.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "candidate_documents", schema = "public")
public class CandidateDocumentsEntity {

    @Id
    @Column(name = "document_id")
    private UUID documentId;

    @Column(name = "candidate_id")
    private UUID candidateId;

    @Column(name = "document_type")
    private String documentType;

    @Column(name = "application_id")
    private UUID applicationId;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_url")
    private String fileUrl;

    @Column(name = "uploaded_date")
    private LocalDateTime uploadedDate;

}
