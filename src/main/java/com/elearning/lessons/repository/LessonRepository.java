package com.elearning.lessons.repository;

import com.elearning.courses.entity.Course;
import com.elearning.lessons.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {
    List<Lesson> findByCourseOrderByOrderIndexAsc(Course course);
    long countByCourse(Course course);
}
