package com.elearning.quizzes.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record QuizRequest(
    @NotBlank @Size(min = 3, max = 200) String title,
    @Size(max = 2000) String description,
    @NotNull Integer durationMinutes,
    @NotNull Integer totalQuestions,
    Integer passScore,
    Integer maxAttempts,
    Boolean shuffleQuestions
) {}
