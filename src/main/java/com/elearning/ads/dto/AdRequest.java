package com.elearning.ads.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AdRequest(
    @NotBlank @Size(max = 255) String title,
    String imageUrl,
    String targetUrl,
    @NotBlank String placementSlot,
    String targetRole,
    String startsAt,
    String endsAt,
    Boolean isActive
) {}
