package com.elearning.payments.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentResponse(
    Long id,
    Long orderId,
    String provider,
    String providerPaymentId,
    BigDecimal amount,
    String currency,
    String status,
    Boolean signatureVerified,
    LocalDateTime paidAt,
    LocalDateTime createdAt
) {}
