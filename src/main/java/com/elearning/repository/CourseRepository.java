package com.elearning.repository;

import com.elearning.entity.Course;
import com.elearning.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByInstructor(User instructor);
    List<Course> findByCategory(String category);
    List<Course> findByTitleContainingIgnoreCase(String title);
}
