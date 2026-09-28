package com.elearning.repository;

import com.elearning.entity.Course;
import com.elearning.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LessonRepository extends JpaRepository<Lesson, Long> {
    List<Lesson> findByCourseOrderByOrderIndexAsc(Course course);
    long countByCourse(Course course);
}
