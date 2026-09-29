package com.elearning.courses.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CourseResponse(
    Long id,
    String title,
    String description,
    String category,
    BigDecimal price,
    String status,
    String level,
    String thumbnailUrl,
    Long instructorId,
    String instructorName,
    LocalDateTime createdAt,
    long lessonCount,
    long enrollmentCount
) {}
