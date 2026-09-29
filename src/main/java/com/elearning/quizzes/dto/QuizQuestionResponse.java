package com.elearning.quizzes.dto;

public record QuizQuestionResponse(
    Long id,
    Long quizId,
    String questionText,
    String optionA,
    String optionB,
    String optionC,
    String optionD
) {}
