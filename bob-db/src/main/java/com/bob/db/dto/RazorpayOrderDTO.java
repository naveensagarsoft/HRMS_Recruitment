package com.bob.db.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
public class RazorpayOrderDTO {
    private Long id;
    private String orderId;
    private Integer amount;
    private String currency;
    private String status;
    private String receipt;
    private Map<String, Object> notes;
    private String paymentId;
    private String signature;
    private Integer capturedAmount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String candidateId;
    private String positionId;
    private String requisitionCode;
}
