package com.elearning.quizzes.dto;

import jakarta.validation.constraints.NotNull;
import java.util.Map;

public record QuizAttemptRequest(
    @NotNull Map<Long, String> answers
) {}
