package com.elearning.media.service;

import com.elearning.common.error.ForbiddenException;
import com.elearning.common.error.NotFoundException;
import com.elearning.common.security.AuthenticatedUser;
import com.elearning.common.security.SecurityUtils;
import com.elearning.courses.entity.Course;
import com.elearning.courses.repository.CourseRepository;
import com.elearning.enrollments.entity.Enrollment;
import com.elearning.enrollments.repository.EnrollmentRepository;
import com.elearning.lessons.entity.Lesson;
import com.elearning.lessons.repository.LessonRepository;
import com.elearning.media.security.StreamTokenService;
import org.springframework.stereotype.Service;

@Service
public class MediaService {

    private final LessonRepository lessonRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final StreamTokenService streamTokenService;

    public MediaService(LessonRepository lessonRepository, CourseRepository courseRepository,
                        EnrollmentRepository enrollmentRepository, StreamTokenService streamTokenService) {
        this.lessonRepository = lessonRepository;
        this.courseRepository = courseRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.streamTokenService = streamTokenService;
    }

    public String issueStreamToken(Long lessonId) {
        AuthenticatedUser currentUser = SecurityUtils.getCurrentUser();
        Lesson lesson = lessonRepository.findById(lessonId)
            .orElseThrow(() -> new NotFoundException("Lesson not found"));

        if (!hasAccess(currentUser, lesson)) {
            throw new ForbiddenException("Not authorized to stream this lesson");
        }

        return streamTokenService.generateToken(currentUser.id(), lessonId);
    }

    public boolean validateStreamAccess(String token, Long lessonId) {
        AuthenticatedUser currentUser = SecurityUtils.getCurrentUser();
        return streamTokenService.validateToken(token, currentUser.id(), lessonId);
    }

    private boolean hasAccess(AuthenticatedUser user, Lesson lesson) {
        if (user.role().equals("ADMIN")) return true;
        if (Boolean.TRUE.equals(lesson.getIsFreePreview())) return true;

        Course course = lesson.getCourse();
        if (course.getInstructor().getId().equals(user.id())) return true;

        com.elearning.users.entity.User student = new com.elearning.users.entity.User();
        student.setId(user.id());
        return enrollmentRepository.findByStudentAndCourse(student, course).isPresent();
    }
}
