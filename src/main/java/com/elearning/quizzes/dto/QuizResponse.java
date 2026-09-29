package com.elearning.quizzes.dto;

import java.time.LocalDateTime;

public record QuizResponse(
    Long id,
    Long courseId,
    String title,
    String description,
    Integer durationMinutes,
    Integer totalQuestions,
    Integer passScore,
    Integer maxAttempts,
    Boolean shuffleQuestions,
    LocalDateTime createdAt
) {}
