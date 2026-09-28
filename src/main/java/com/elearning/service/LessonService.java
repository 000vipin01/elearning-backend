package com.elearning.service;

import com.elearning.entity.Course;
import com.elearning.entity.Lesson;
import com.elearning.repository.CourseRepository;
import com.elearning.repository.LessonRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LessonService {

    private final LessonRepository lessonRepository;
    private final CourseRepository courseRepository;

    public LessonService(LessonRepository lessonRepository, CourseRepository courseRepository) {
        this.lessonRepository = lessonRepository;
        this.courseRepository = courseRepository;
    }

    public List<Lesson> getLessonsByCourse(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        return lessonRepository.findByCourseOrderByOrderIndexAsc(course);
    }

    public Lesson getLesson(Long courseId, Long lessonId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        return lessonRepository.findById(lessonId)
                .filter(l -> l.getCourse().getId().equals(courseId))
                .orElseThrow(() -> new RuntimeException("Lesson not found"));
    }

    public Lesson createLesson(Long courseId, String title, String content, Integer orderIndex, Integer durationMinutes, Long instructorId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        if (!course.getInstructor().getId().equals(instructorId)) {
            throw new RuntimeException("Not authorized to add lessons to this course");
        }

        Lesson lesson = new Lesson();
        lesson.setCourse(course);
        lesson.setTitle(title);
        lesson.setContent(content);
        lesson.setOrderIndex(orderIndex);
        lesson.setDurationMinutes(durationMinutes);

        return lessonRepository.save(lesson);
    }

    public void deleteLesson(Long courseId, Long lessonId, Long instructorId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        if (!course.getInstructor().getId().equals(instructorId)) {
            throw new RuntimeException("Not authorized to delete lessons from this course");
        }

        lessonRepository.deleteById(lessonId);
    }
}
