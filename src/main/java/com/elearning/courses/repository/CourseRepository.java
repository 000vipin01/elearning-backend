package com.elearning.courses.repository;

import com.elearning.courses.entity.Course;
import com.elearning.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByInstructor(User instructor);
    List<Course> findByStatus(String status);
    List<Course> findByTitleContainingIgnoreCase(String title);
    long countByInstructor(User instructor);
}
