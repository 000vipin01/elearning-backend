package com.elearning.coupons.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record CouponRequest(
    @NotBlank @Size(max = 50) String code,
    String description,
    @NotBlank String discountType,
    @NotNull @Positive BigDecimal discountValue,
    Integer maxUses,
    Integer maxUsesPerUser,
    @PositiveOrZero BigDecimal minOrderAmount,
    String startsAt,
    String expiresAt
) {}
