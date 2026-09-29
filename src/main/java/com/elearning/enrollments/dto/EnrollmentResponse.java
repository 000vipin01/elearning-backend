package com.elearning.enrollments.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record EnrollmentResponse(
    Long id,
    Long studentId,
    Long courseId,
    String courseTitle,
    Integer progress,
    LocalDateTime enrolledAt,
    LocalDateTime completedAt,
    Long orderId,
    BigDecimal pricePaid
) {}
