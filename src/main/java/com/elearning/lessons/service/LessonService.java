package com.elearning.lessons.service;

import com.elearning.common.error.ForbiddenException;
import com.elearning.common.error.NotFoundException;
import com.elearning.common.security.AuthenticatedUser;
import com.elearning.common.security.SecurityUtils;
import com.elearning.courses.entity.Course;
import com.elearning.courses.repository.CourseRepository;
import com.elearning.lessons.dto.LessonRequest;
import com.elearning.lessons.dto.LessonResponse;
import com.elearning.lessons.entity.Lesson;
import com.elearning.lessons.repository.LessonRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LessonService {

    private final LessonRepository lessonRepository;
    private final CourseRepository courseRepository;

    public LessonService(LessonRepository lessonRepository, CourseRepository courseRepository) {
        this.lessonRepository = lessonRepository;
        this.courseRepository = courseRepository;
    }

    @Transactional
    public LessonResponse createLesson(Long courseId, LessonRequest request) {
        Course course = getCourse(courseId);
        AuthenticatedUser currentUser = SecurityUtils.getCurrentUser();

        if (!course.getInstructor().getId().equals(currentUser.id()) && !currentUser.role().equals("ADMIN")) {
            throw new ForbiddenException("Not authorized to add lessons to this course");
        }

        Lesson lesson = new Lesson();
        lesson.setCourse(course);
        lesson.setTitle(request.title());
        lesson.setContent(request.content());
        lesson.setOrderIndex(request.orderIndex());
        lesson.setDurationMinutes(request.durationMinutes());
        lesson.setIsFreePreview(request.isFreePreview() != null && request.isFreePreview());
        lesson.setMediaType(request.mediaType());
        lesson.setMediaPath(request.mediaPath());
        lesson.setMediaDuration(request.mediaDuration());
        lesson.setMediaSize(request.mediaSize());
        lesson.setMediaMime(request.mediaMime());

        lessonRepository.save(lesson);
        return toResponse(lesson);
    }

    @Transactional
    public LessonResponse updateLesson(Long courseId, Long lessonId, LessonRequest request) {
        Lesson lesson = getLessonEntity(courseId, lessonId);
        AuthenticatedUser currentUser = SecurityUtils.getCurrentUser();

        if (!lesson.getCourse().getInstructor().getId().equals(currentUser.id()) && !currentUser.role().equals("ADMIN")) {
            throw new ForbiddenException("Not authorized to update this lesson");
        }

        lesson.setTitle(request.title());
        lesson.setContent(request.content());
        lesson.setOrderIndex(request.orderIndex());
        lesson.setDurationMinutes(request.durationMinutes());
        lesson.setIsFreePreview(request.isFreePreview() != null && request.isFreePreview());
        lesson.setMediaType(request.mediaType());
        lesson.setMediaPath(request.mediaPath());
        lesson.setMediaDuration(request.mediaDuration());
        lesson.setMediaSize(request.mediaSize());
        lesson.setMediaMime(request.mediaMime());

        lessonRepository.save(lesson);
        return toResponse(lesson);
    }

    @Transactional
    public void deleteLesson(Long courseId, Long lessonId) {
        Lesson lesson = getLessonEntity(courseId, lessonId);
        AuthenticatedUser currentUser = SecurityUtils.getCurrentUser();

        if (!lesson.getCourse().getInstructor().getId().equals(currentUser.id()) && !currentUser.role().equals("ADMIN")) {
            throw new ForbiddenException("Not authorized to delete this lesson");
        }

        lessonRepository.delete(lesson);
    }

    public List<LessonResponse> getLessons(Long courseId) {
        Course course = getCourse(courseId);
        AuthenticatedUser currentUser = SecurityUtils.getCurrentUser();

        boolean isOwner = course.getInstructor().getId().equals(currentUser.id());
        boolean isAdmin = currentUser.role().equals("ADMIN");

        if (!"PUBLISHED".equals(course.getStatus()) && !isOwner && !isAdmin) {
            throw new NotFoundException("Course not found");
        }

        return lessonRepository.findByCourseOrderByOrderIndexAsc(course).stream()
            .map(this::toResponse)
            .toList();
    }

    public LessonResponse getLesson(Long courseId, Long lessonId) {
        Lesson lesson = getLessonEntity(courseId, lessonId);
        return toResponse(lesson);
    }

    private Course getCourse(Long courseId) {
        return courseRepository.findById(courseId)
            .orElseThrow(() -> new NotFoundException("Course not found"));
    }

    private Lesson getLessonEntity(Long courseId, Long lessonId) {
        Course course = getCourse(courseId);
        return lessonRepository.findById(lessonId)
            .filter(l -> l.getCourse().getId().equals(courseId))
            .orElseThrow(() -> new NotFoundException("Lesson not found"));
    }

    private LessonResponse toResponse(Lesson lesson) {
        return new LessonResponse(
            lesson.getId(), lesson.getCourse().getId(), lesson.getTitle(), lesson.getContent(),
            lesson.getOrderIndex(), lesson.getDurationMinutes(), lesson.getIsFreePreview(),
            lesson.getMediaType(), lesson.getMediaPath(), lesson.getMediaDuration(),
            lesson.getMediaSize(), lesson.getMediaMime()
        );
    }
}
