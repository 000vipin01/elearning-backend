package com.elearning.offers.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OfferResponse(
    Long id,
    Long courseId,
    String title,
    String description,
    BigDecimal discountPercentage,
    LocalDateTime startsAt,
    LocalDateTime endsAt,
    String status,
    LocalDateTime createdAt
) {}
