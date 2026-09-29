package com.elearning.enrollments.service;

import com.elearning.common.error.ConflictException;
import com.elearning.common.error.ForbiddenException;
import com.elearning.common.error.NotFoundException;
import com.elearning.common.security.AuthenticatedUser;
import com.elearning.common.security.SecurityUtils;
import com.elearning.courses.entity.Course;
import com.elearning.courses.repository.CourseRepository;
import com.elearning.enrollments.dto.EnrollmentResponse;
import com.elearning.enrollments.entity.Enrollment;
import com.elearning.enrollments.repository.EnrollmentRepository;
import com.elearning.lessons.entity.Lesson;
import com.elearning.lessons.repository.LessonRepository;
import com.elearning.users.entity.User;
import com.elearning.users.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final LessonRepository lessonRepository;

    public EnrollmentService(EnrollmentRepository enrollmentRepository, CourseRepository courseRepository,
                             UserRepository userRepository, LessonRepository lessonRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
        this.lessonRepository = lessonRepository;
    }

    @Transactional
    public EnrollmentResponse enroll(Long courseId) {
        AuthenticatedUser currentUser = SecurityUtils.getCurrentUser();
        User student = userRepository.findById(currentUser.id())
            .orElseThrow(() -> new NotFoundException("User not found"));
        Course course = courseRepository.findById(courseId)
            .orElseThrow(() -> new NotFoundException("Course not found"));

        if (!"PUBLISHED".equals(course.getStatus())) {
            throw new ForbiddenException("Course is not available for enrollment");
        }

        if (enrollmentRepository.existsByStudentAndCourse(student, course)) {
            throw new ConflictException("Already enrolled in this course");
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setProgress(0);
        enrollment.setPricePaid(course.getPrice());

        enrollmentRepository.save(enrollment);
        return toResponse(enrollment);
    }

    public List<EnrollmentResponse> getMyEnrollments() {
        AuthenticatedUser currentUser = SecurityUtils.getCurrentUser();
        User student = userRepository.findById(currentUser.id())
            .orElseThrow(() -> new NotFoundException("User not found"));
        return enrollmentRepository.findByStudent(student).stream().map(this::toResponse).toList();
    }

    public EnrollmentResponse getEnrollment(Long courseId) {
        AuthenticatedUser currentUser = SecurityUtils.getCurrentUser();
        User student = userRepository.findById(currentUser.id())
            .orElseThrow(() -> new NotFoundException("User not found"));
        Course course = courseRepository.findById(courseId)
            .orElseThrow(() -> new NotFoundException("Course not found"));

        Enrollment enrollment = enrollmentRepository.findByStudentAndCourse(student, course)
            .orElseThrow(() -> new NotFoundException("Not enrolled in this course"));

        return toResponse(enrollment);
    }

    @Transactional
    public EnrollmentResponse updateProgress(Long courseId, Integer progress) {
        AuthenticatedUser currentUser = SecurityUtils.getCurrentUser();
        User student = userRepository.findById(currentUser.id())
            .orElseThrow(() -> new NotFoundException("User not found"));
        Course course = courseRepository.findById(courseId)
            .orElseThrow(() -> new NotFoundException("Course not found"));

        Enrollment enrollment = enrollmentRepository.findByStudentAndCourse(student, course)
            .orElseThrow(() -> new NotFoundException("Not enrolled in this course"));

        enrollment.setProgress(Math.min(100, Math.max(0, progress)));
        if (enrollment.getProgress() == 100) {
            enrollment.setCompletedAt(LocalDateTime.now());
        }

        enrollmentRepository.save(enrollment);
        return toResponse(enrollment);
    }

    public List<EnrollmentResponse> getCourseEnrollments(Long courseId) {
        Course course = courseRepository.findById(courseId)
            .orElseThrow(() -> new NotFoundException("Course not found"));
        return enrollmentRepository.findByCourse(course).stream().map(this::toResponse).toList();
    }

    private EnrollmentResponse toResponse(Enrollment enrollment) {
        return new EnrollmentResponse(
            enrollment.getId(), enrollment.getStudent().getId(), enrollment.getCourse().getId(),
            enrollment.getCourse().getTitle(), enrollment.getProgress(),
            enrollment.getEnrolledAt(), enrollment.getCompletedAt(),
            enrollment.getOrderId(), enrollment.getPricePaid()
        );
    }
}
