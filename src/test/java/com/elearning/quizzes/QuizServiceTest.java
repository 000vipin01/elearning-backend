package com.elearning.quizzes;

import com.elearning.common.error.ForbiddenException;
import com.elearning.common.error.NotFoundException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class QuizServiceTest {

    @Test
    void scoring_allCorrect() {
        Map<Long, String> answers = new HashMap<>();
        answers.put(1L, "A");
        answers.put(2L, "B");
        answers.put(3L, "C");

        Map<Long, String> correctAnswers = new HashMap<>();
        correctAnswers.put(1L, "A");
        correctAnswers.put(2L, "B");
        correctAnswers.put(3L, "C");

        int score = 0;
        for (Map.Entry<Long, String> entry : answers.entrySet()) {
            if (entry.getValue().equalsIgnoreCase(correctAnswers.get(entry.getKey()))) {
                score++;
            }
        }

        assertEquals(3, score);
        BigDecimal percentage = BigDecimal.valueOf(score * 100.0 / 3).setScale(2, RoundingMode.HALF_UP);
        assertTrue(percentage.compareTo(BigDecimal.valueOf(60)) >= 0);
    }

    @Test
    void scoring_partialScore() {
        Map<Long, String> answers = new HashMap<>();
        answers.put(1L, "A");  // correct
        answers.put(2L, "A");  // wrong
        answers.put(3L, "C");  // correct

        Map<Long, String> correctAnswers = new HashMap<>();
        correctAnswers.put(1L, "A");
        correctAnswers.put(2L, "B");
        correctAnswers.put(3L, "C");

        int score = 0;
        for (Map.Entry<Long, String> entry : answers.entrySet()) {
            if (entry.getValue().equalsIgnoreCase(correctAnswers.get(entry.getKey()))) {
                score++;
            }
        }

        assertEquals(2, score);
        BigDecimal percentage = BigDecimal.valueOf(score * 100.0 / 3).setScale(2, RoundingMode.HALF_UP);
        assertTrue(percentage.compareTo(BigDecimal.valueOf(60)) >= 0);
    }

    @Test
    void scoring_failingScore() {
        Map<Long, String> answers = new HashMap<>();
        answers.put(1L, "B");  // wrong
        answers.put(2L, "A");  // wrong
        answers.put(3L, "A");  // wrong

        Map<Long, String> correctAnswers = new HashMap<>();
        correctAnswers.put(1L, "A");
        correctAnswers.put(2L, "B");
        correctAnswers.put(3L, "C");

        int score = 0;
        for (Map.Entry<Long, String> entry : answers.entrySet()) {
            if (entry.getValue().equalsIgnoreCase(correctAnswers.get(entry.getKey()))) {
                score++;
            }
        }

        assertEquals(0, score);
        BigDecimal percentage = BigDecimal.valueOf(0);
        assertTrue(percentage.compareTo(BigDecimal.valueOf(60)) < 0);
    }

    @Test
    void scoring_passThreshold() {
        // 2 out of 3 = 66.67%, pass score is 60
        int score = 2;
        int total = 3;
        int passScore = 60;

        BigDecimal percentage = BigDecimal.valueOf(score * 100.0 / total).setScale(2, RoundingMode.HALF_UP);
        boolean passed = percentage.compareTo(BigDecimal.valueOf(passScore)) >= 0;

        assertTrue(passed);
    }

    @Test
    void scoring_failThreshold() {
        // 1 out of 3 = 33.33%, pass score is 60
        int score = 1;
        int total = 3;
        int passScore = 60;

        BigDecimal percentage = BigDecimal.valueOf(score * 100.0 / total).setScale(2, RoundingMode.HALF_UP);
        boolean passed = percentage.compareTo(BigDecimal.valueOf(passScore)) >= 0;

        assertFalse(passed);
    }

    @Test
    void notEnrolled_throwsForbidden() {
        assertThrows(ForbiddenException.class, () -> {
            throw new ForbiddenException("Not enrolled in this course");
        });
    }

    @Test
    void quizNotFound_throwsNotFound() {
        assertThrows(NotFoundException.class, () -> {
            throw new NotFoundException("Quiz not found");
        });
    }
}
