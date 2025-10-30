package com.bob.db.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserDTO {
    @JsonProperty("userid")
    private int userId;

    @JsonProperty("name")
    private String name;

    @JsonProperty("role")
    private String role;

    @JsonProperty("email")
    private String email;

    @JsonProperty("created_date")
    private LocalDateTime createdDate;

    @JsonProperty("created_by")
    private String createdBy;
}