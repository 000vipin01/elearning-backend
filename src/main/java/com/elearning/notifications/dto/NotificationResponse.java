package com.elearning.notifications.dto;

import java.time.LocalDateTime;

public record NotificationResponse(
    Long id,
    String type,
    String title,
    String body,
    String link,
    Boolean isRead,
    LocalDateTime readAt,
    LocalDateTime createdAt
) {}
