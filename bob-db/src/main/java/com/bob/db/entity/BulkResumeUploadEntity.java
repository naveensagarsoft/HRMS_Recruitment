package com.bob.db.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name = "bulk_resume_uploads", schema = "public")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class BulkResumeUploadEntity {

	public enum Status {
		UPLOADED,
		PROCESSING,
		COMPLETED,
		FAILED
	}

	@Id
	@Column(name = "resume_id", columnDefinition = "uuid")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID resumeId;

	@Column(name = "original_filename")
	private String originalFilename;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", length = 50)
	private Status status;

	@Column(name = "file_url")
	private String fileUrl;

	@Column(name = "failed_reason")
	private String failedReason;

	@Column(name = "created_by", length = 255)
    @CreatedBy
    private String createdBy;

	@Column(name = "created_date")
    @CreationTimestamp
	private Timestamp createdDate;

	@Column(name = "updated_by", length = 255)
    @LastModifiedBy
	private String updatedBy;

	@Column(name = "updated_date")
    @UpdateTimestamp
	private Timestamp updatedDate;

	@Column(name = "original_filepath")
	private String originalFilepath;

	@Column(name = "parsed_raw_data")
	private String parsedRawData;

	@Column(name = "json_data", columnDefinition = "jsonb")
	private String jsonData;

}
