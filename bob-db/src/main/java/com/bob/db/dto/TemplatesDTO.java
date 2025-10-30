package com.bob.db.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class TemplatesDTO {

    @JsonProperty("template_id")
    private Integer templateId;

    @JsonProperty("template_type")
    private String templateType;

    @JsonProperty("template_name")
    private String templateName;

    @JsonProperty("template_desc")
    private String templateDesc;

    @JsonProperty("file_path")
    private String filePath;

    @JsonProperty("others")
    private String others;

    @JsonProperty("created_by")
    private String createdBy;

    @JsonProperty("created_date")
    private LocalDateTime createdDate;

    @JsonProperty("updated_by")
    private String updatedBy;

    @JsonProperty("updated_date")
    private LocalDateTime updatedDate;
}
