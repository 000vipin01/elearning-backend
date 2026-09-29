package com.elearning.quizzes.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record QuizAttemptResponse(
    Long id,
    Long quizId,
    Integer score,
    Integer totalQuestions,
    BigDecimal percentage,
    Boolean passed,
    LocalDateTime completedAt
) {}
