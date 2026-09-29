package com.elearning.quizzes.controller;

import com.elearning.quizzes.dto.*;
import com.elearning.quizzes.service.QuizService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/courses/{courseId}/quizzes")
public class QuizController {

    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @GetMapping
    public ResponseEntity<List<QuizResponse>> getQuizzes(@PathVariable Long courseId) {
        return ResponseEntity.ok(quizService.getQuizzes(courseId));
    }

    @GetMapping("/{quizId}")
    public ResponseEntity<QuizWithQuestionsResponse> getQuiz(@PathVariable Long courseId, @PathVariable Long quizId) {
        return ResponseEntity.ok(quizService.getQuizWithQuestions(courseId, quizId));
    }

    @PostMapping
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<QuizResponse> createQuiz(@PathVariable Long courseId, @Valid @RequestBody QuizRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(quizService.createQuiz(courseId, request));
    }

    @PutMapping("/{quizId}")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<QuizResponse> updateQuiz(@PathVariable Long courseId, @PathVariable Long quizId, @Valid @RequestBody QuizRequest request) {
        return ResponseEntity.ok(quizService.updateQuiz(courseId, quizId, request));
    }

    @DeleteMapping("/{quizId}")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<Void> deleteQuiz(@PathVariable Long courseId, @PathVariable Long quizId) {
        quizService.deleteQuiz(courseId, quizId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{quizId}/questions")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<QuizQuestionResponse> addQuestion(@PathVariable Long courseId, @PathVariable Long quizId, @Valid @RequestBody QuizQuestionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(quizService.addQuestion(courseId, quizId, request));
    }

    @PostMapping("/{quizId}/submit")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<QuizAttemptResponse> submitQuiz(@PathVariable Long courseId, @PathVariable Long quizId, @Valid @RequestBody QuizAttemptRequest request) {
        return ResponseEntity.ok(quizService.submitQuiz(courseId, quizId, request));
    }

    @GetMapping("/{quizId}/attempts")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<QuizAttemptResponse>> getMyAttempts(@PathVariable Long courseId, @PathVariable Long quizId) {
        return ResponseEntity.ok(quizService.getMyAttempts(quizId));
    }

    @GetMapping("/{quizId}/best-score")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<QuizAttemptResponse> getBestScore(@PathVariable Long courseId, @PathVariable Long quizId) {
        return ResponseEntity.ok(quizService.getBestScore(quizId));
    }
}
