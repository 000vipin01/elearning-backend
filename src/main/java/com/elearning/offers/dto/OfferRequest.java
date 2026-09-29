package com.elearning.offers.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record OfferRequest(
    @NotNull Long courseId,
    @NotBlank String title,
    String description,
    @NotNull @Positive BigDecimal discountPercentage,
    @NotNull String startsAt,
    @NotNull String endsAt
) {}
