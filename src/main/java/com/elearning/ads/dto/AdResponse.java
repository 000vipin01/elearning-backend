package com.elearning.ads.dto;

import java.time.LocalDateTime;

public record AdResponse(
    Long id,
    String title,
    String imageUrl,
    String targetUrl,
    String placementSlot,
    String targetRole,
    LocalDateTime startsAt,
    LocalDateTime endsAt,
    Boolean isActive,
    Integer impressions,
    Integer clicks,
    LocalDateTime createdAt
) {}
