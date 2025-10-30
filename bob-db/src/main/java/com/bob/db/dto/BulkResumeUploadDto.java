package com.bob.db.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.UUID;

@Data
public class BulkResumeUploadDto {

    @JsonProperty(value = "resume_id", access = JsonProperty.Access.READ_ONLY)
    private UUID resumeId;

    @JsonProperty("original_filename")
    private String originalFilename;

    @JsonProperty("status")
    private String status;

    @JsonProperty(value = "file_url", access = JsonProperty.Access.READ_ONLY)
    private String fileUrl;

    @JsonProperty("created_by")
    private String createdBy;

    @JsonProperty(value = "created_date", access = JsonProperty.Access.READ_ONLY)
    private Timestamp createdDate;

    @JsonProperty(value = "updated_by", access = JsonProperty.Access.READ_ONLY)
    private String updatedBy;

    @JsonProperty(value = "updated_date", access = JsonProperty.Access.READ_ONLY)
    private Timestamp updatedDate;

    @JsonProperty("failed_reason")
    private String failedReason;
}
