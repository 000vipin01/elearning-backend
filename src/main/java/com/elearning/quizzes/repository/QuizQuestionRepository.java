package com.elearning.quizzes.repository;

import com.elearning.quizzes.entity.Quiz;
import com.elearning.quizzes.entity.QuizQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizQuestionRepository extends JpaRepository<QuizQuestion, Long> {
    List<QuizQuestion> findByQuiz(Quiz quiz);
}
