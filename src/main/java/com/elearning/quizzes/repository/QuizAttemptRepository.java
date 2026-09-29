package com.elearning.quizzes.repository;

import com.elearning.quizzes.entity.Quiz;
import com.elearning.quizzes.entity.QuizAttempt;
import com.elearning.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizAttemptRepository extends JpaRepository<QuizAttempt, Long> {
    List<QuizAttempt> findByStudentAndQuiz(User student, Quiz quiz);
    List<QuizAttempt> findByStudent(User student);
}
