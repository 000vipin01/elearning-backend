package com.elearning.quizzes.dto;

import java.util.List;

public record QuizWithQuestionsResponse(
    QuizResponse quiz,
    List<QuizQuestionResponse> questions
) {}
