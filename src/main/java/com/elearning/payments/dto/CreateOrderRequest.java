package com.elearning.payments.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateOrderRequest(
    @NotNull Long courseId,
    String couponCode,
    String idempotencyKey
) {}
