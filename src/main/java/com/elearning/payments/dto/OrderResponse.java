package com.elearning.payments.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderResponse(
    Long id,
    Long studentId,
    String status,
    BigDecimal totalAmount,
    BigDecimal discountAmount,
    BigDecimal finalAmount,
    String idempotencyKey,
    LocalDateTime createdAt
) {}
