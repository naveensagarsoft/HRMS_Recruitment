package com.bob.db.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Table(name = "razorpay_orders", schema = "public")
@Data
public class RazorpayOrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id", nullable = false)
    private String orderId;

    @Column(nullable = false)
    private Integer amount;

    @Column(length = 10, nullable = false)
    private String currency;

    @Column(length = 50, nullable = false)
    private String status;

    @Column
    private String receipt;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> notes;

    @Column(name = "payment_id")
    private String paymentId;

    @Column(columnDefinition = "text")
    private String signature;

    @Column(name = "captured_amount")
    private Integer capturedAmount;

    @Column(name = "created_at", columnDefinition = "timestamp default now()")
    private LocalDateTime createdAt;

    @Column(name = "updated_at", columnDefinition = "timestamp default now()")
    private LocalDateTime updatedAt;

    @Column(name = "candidate_id")
    private String candidateId;

    @Column(name = "position_id")
    private String positionId;

}
