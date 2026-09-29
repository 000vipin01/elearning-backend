package com.elearning.courses.repository;

import com.elearning.courses.entity.Course;
import com.elearning.users.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByInstructor(User instructor);
    List<Course> findByStatus(String status);
    List<Course> findByTitleContainingIgnoreCase(String title);
    Page<Course> findByStatus(String status, Pageable pageable);
    Page<Course> findByTitleContainingIgnoreCaseAndStatus(String title, String status, Pageable pageable);
    Page<Course> findByCategoryAndStatus(String category, String status, Pageable pageable);
    long countByInstructor(User instructor);
}
