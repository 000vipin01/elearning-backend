package com.elearning.quizzes.service;

import com.elearning.common.error.ForbiddenException;
import com.elearning.common.error.NotFoundException;
import com.elearning.common.security.AuthenticatedUser;
import com.elearning.common.security.SecurityUtils;
import com.elearning.courses.entity.Course;
import com.elearning.courses.repository.CourseRepository;
import com.elearning.enrollments.entity.Enrollment;
import com.elearning.enrollments.repository.EnrollmentRepository;
import com.elearning.quizzes.dto.*;
import com.elearning.quizzes.entity.Quiz;
import com.elearning.quizzes.entity.QuizAttempt;
import com.elearning.quizzes.entity.QuizQuestion;
import com.elearning.quizzes.repository.QuizAttemptRepository;
import com.elearning.quizzes.repository.QuizQuestionRepository;
import com.elearning.quizzes.repository.QuizRepository;
import com.elearning.users.entity.User;
import com.elearning.users.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class QuizService {

    private final QuizRepository quizRepository;
    private final QuizQuestionRepository quizQuestionRepository;
    private final QuizAttemptRepository quizAttemptRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final EnrollmentRepository enrollmentRepository;

    public QuizService(QuizRepository quizRepository, QuizQuestionRepository quizQuestionRepository,
                       QuizAttemptRepository quizAttemptRepository, CourseRepository courseRepository,
                       UserRepository userRepository, EnrollmentRepository enrollmentRepository) {
        this.quizRepository = quizRepository;
        this.quizQuestionRepository = quizQuestionRepository;
        this.quizAttemptRepository = quizAttemptRepository;
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
        this.enrollmentRepository = enrollmentRepository;
    }

    @Transactional
    public QuizResponse createQuiz(Long courseId, QuizRequest request) {
        Course course = getCourse(courseId);
        AuthenticatedUser currentUser = SecurityUtils.getCurrentUser();

        if (!course.getInstructor().getId().equals(currentUser.id()) && !currentUser.role().equals("ADMIN")) {
            throw new ForbiddenException("Not authorized to create quizzes for this course");
        }

        Quiz quiz = new Quiz();
        quiz.setCourse(course);
        quiz.setTitle(request.title());
        quiz.setDescription(request.description());
        quiz.setDurationMinutes(request.durationMinutes());
        quiz.setTotalQuestions(request.totalQuestions());
        quiz.setPassScore(request.passScore() != null ? request.passScore() : 60);
        quiz.setMaxAttempts(request.maxAttempts() != null ? request.maxAttempts() : 0);
        quiz.setShuffleQuestions(request.shuffleQuestions() != null && request.shuffleQuestions());

        quizRepository.save(quiz);
        return toResponse(quiz);
    }

    @Transactional
    public QuizResponse updateQuiz(Long courseId, Long quizId, QuizRequest request) {
        Quiz quiz = getQuiz(courseId, quizId);
        AuthenticatedUser currentUser = SecurityUtils.getCurrentUser();

        if (!quiz.getCourse().getInstructor().getId().equals(currentUser.id()) && !currentUser.role().equals("ADMIN")) {
            throw new ForbiddenException("Not authorized to update this quiz");
        }

        quiz.setTitle(request.title());
        quiz.setDescription(request.description());
        quiz.setDurationMinutes(request.durationMinutes());
        quiz.setTotalQuestions(request.totalQuestions());
        quiz.setPassScore(request.passScore() != null ? request.passScore() : 60);
        quiz.setMaxAttempts(request.maxAttempts() != null ? request.maxAttempts() : 0);
        quiz.setShuffleQuestions(request.shuffleQuestions() != null && request.shuffleQuestions());

        quizRepository.save(quiz);
        return toResponse(quiz);
    }

    @Transactional
    public void deleteQuiz(Long courseId, Long quizId) {
        Quiz quiz = getQuiz(courseId, quizId);
        AuthenticatedUser currentUser = SecurityUtils.getCurrentUser();

        if (!quiz.getCourse().getInstructor().getId().equals(currentUser.id()) && !currentUser.role().equals("ADMIN")) {
            throw new ForbiddenException("Not authorized to delete this quiz");
        }

        quizRepository.delete(quiz);
    }

    public List<QuizResponse> getQuizzes(Long courseId) {
        Course course = getCourse(courseId);
        return quizRepository.findByCourse(course).stream().map(this::toResponse).toList();
    }

    public QuizWithQuestionsResponse getQuizWithQuestions(Long courseId, Long quizId) {
        Quiz quiz = getQuiz(courseId, quizId);
        List<QuizQuestionResponse> questions = quizQuestionRepository.findByQuiz(quiz).stream()
            .map(this::toQuestionResponse)
            .toList();
        return new QuizWithQuestionsResponse(toResponse(quiz), questions);
    }

    @Transactional
    public QuizQuestionResponse addQuestion(Long courseId, Long quizId, QuizQuestionRequest request) {
        Quiz quiz = getQuiz(courseId, quizId);
        AuthenticatedUser currentUser = SecurityUtils.getCurrentUser();

        if (!quiz.getCourse().getInstructor().getId().equals(currentUser.id()) && !currentUser.role().equals("ADMIN")) {
            throw new ForbiddenException("Not authorized to add questions to this quiz");
        }

        QuizQuestion question = new QuizQuestion();
        question.setQuiz(quiz);
        question.setQuestionText(request.questionText());
        question.setOptionA(request.optionA());
        question.setOptionB(request.optionB());
        question.setOptionC(request.optionC());
        question.setOptionD(request.optionD());
        question.setCorrectOption(request.correctOption());

        quizQuestionRepository.save(question);
        return toQuestionResponse(question);
    }

    @Transactional
    public QuizAttemptResponse submitQuiz(Long courseId, Long quizId, QuizAttemptRequest request) {
        AuthenticatedUser currentUser = SecurityUtils.getCurrentUser();
        User student = userRepository.findById(currentUser.id())
            .orElseThrow(() -> new NotFoundException("User not found"));
        Course course = getCourse(courseId);
        Quiz quiz = getQuiz(courseId, quizId);

        Enrollment enrollment = enrollmentRepository.findByStudentAndCourse(student, course)
            .orElseThrow(() -> new ForbiddenException("Not enrolled in this course"));

        List<QuizQuestion> questions = quizQuestionRepository.findByQuiz(quiz);
        if (questions.isEmpty()) {
            throw new NotFoundException("No questions found for this quiz");
        }

        int score = 0;
        for (QuizQuestion q : questions) {
            String answer = request.answers().get(q.getId());
            if (answer != null && answer.equalsIgnoreCase(q.getCorrectOption())) {
                score++;
            }
        }

        int total = questions.size();
        BigDecimal percentage = BigDecimal.valueOf(score * 100.0 / total).setScale(2, RoundingMode.HALF_UP);
        boolean passed = percentage.compareTo(BigDecimal.valueOf(quiz.getPassScore())) >= 0;

        QuizAttempt attempt = new QuizAttempt();
        attempt.setQuiz(quiz);
        attempt.setStudent(student);
        attempt.setScore(score);
        attempt.setTotalQuestions(total);
        attempt.setPercentage(percentage);
        attempt.setPassed(passed);
        attempt.setAnswers(request.answers().toString());
        attempt.setCompletedAt(LocalDateTime.now());

        quizAttemptRepository.save(attempt);

        return new QuizAttemptResponse(attempt.getId(), quiz.getId(), score, total, percentage, passed, attempt.getCompletedAt());
    }

    public List<QuizAttemptResponse> getMyAttempts(Long quizId) {
        AuthenticatedUser currentUser = SecurityUtils.getCurrentUser();
        User student = userRepository.findById(currentUser.id())
            .orElseThrow(() -> new NotFoundException("User not found"));
        Quiz quiz = quizRepository.findById(quizId)
            .orElseThrow(() -> new NotFoundException("Quiz not found"));

        return quizAttemptRepository.findByStudentAndQuiz(student, quiz).stream()
            .map(this::toAttemptResponse)
            .toList();
    }

    public QuizAttemptResponse getBestScore(Long quizId) {
        AuthenticatedUser currentUser = SecurityUtils.getCurrentUser();
        User student = userRepository.findById(currentUser.id())
            .orElseThrow(() -> new NotFoundException("User not found"));
        Quiz quiz = quizRepository.findById(quizId)
            .orElseThrow(() -> new NotFoundException("Quiz not found"));

        return quizAttemptRepository.findByStudentAndQuiz(student, quiz).stream()
            .max(Comparator.comparing(QuizAttempt::getPercentage))
            .map(this::toAttemptResponse)
            .orElse(null);
    }

    private Course getCourse(Long courseId) {
        return courseRepository.findById(courseId)
            .orElseThrow(() -> new NotFoundException("Course not found"));
    }

    private Quiz getQuiz(Long courseId, Long quizId) {
        Course course = getCourse(courseId);
        return quizRepository.findById(quizId)
            .filter(q -> q.getCourse().getId().equals(courseId))
            .orElseThrow(() -> new NotFoundException("Quiz not found"));
    }

    private QuizResponse toResponse(Quiz quiz) {
        return new QuizResponse(
            quiz.getId(), quiz.getCourse().getId(), quiz.getTitle(), quiz.getDescription(),
            quiz.getDurationMinutes(), quiz.getTotalQuestions(), quiz.getPassScore(),
            quiz.getMaxAttempts(), quiz.getShuffleQuestions(), quiz.getCreatedAt()
        );
    }

    private QuizQuestionResponse toQuestionResponse(QuizQuestion q) {
        return new QuizQuestionResponse(q.getId(), q.getQuiz().getId(), q.getQuestionText(),
            q.getOptionA(), q.getOptionB(), q.getOptionC(), q.getOptionD());
    }

    private QuizAttemptResponse toAttemptResponse(QuizAttempt a) {
        return new QuizAttemptResponse(a.getId(), a.getQuiz().getId(), a.getScore(),
            a.getTotalQuestions(), a.getPercentage(), a.getPassed(), a.getCompletedAt());
    }
}
