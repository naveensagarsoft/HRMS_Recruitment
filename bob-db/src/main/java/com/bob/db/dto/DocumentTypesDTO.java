package com.bob.db.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import lombok.Data;

@Data
public class DocumentTypesDTO {
    @JsonProperty("document_id")
    private Long documentId;

    @JsonProperty("document_name")
    private String documentName;

    @JsonProperty("document_desc")
    private String documentDesc;

}
