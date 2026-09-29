package com.elearning.coupons.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CouponResponse(
    Long id,
    String code,
    String description,
    String discountType,
    BigDecimal discountValue,
    Integer maxUses,
    Integer usedCount,
    Integer maxUsesPerUser,
    BigDecimal minOrderAmount,
    LocalDateTime startsAt,
    LocalDateTime expiresAt,
    Boolean isActive,
    LocalDateTime createdAt
) {}
