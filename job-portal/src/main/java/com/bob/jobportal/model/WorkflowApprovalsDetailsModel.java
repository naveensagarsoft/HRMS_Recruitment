package com.bob.jobportal.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class WorkflowApprovalsDetailsModel {
    private String userName;
    private String status;
    private String mail;
    private LocalDateTime dateTime;
    private String comments;
}
