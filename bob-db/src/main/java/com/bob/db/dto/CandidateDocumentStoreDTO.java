package com.bob.db.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Data
public class CandidateDocumentStoreDTO {
    @JsonProperty("document_store_id")
    private UUID documentStoreId;

    @JsonProperty("candidate_id")
    private UUID candidateId;

    @JsonProperty("document_id")
    private Long documentId;

    @JsonProperty("file_name")
    private String fileName;

    @JsonProperty("file_url")
    private String fileUrl;
}
