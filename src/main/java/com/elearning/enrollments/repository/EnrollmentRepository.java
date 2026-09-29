package com.elearning.enrollments.repository;

import com.elearning.courses.entity.Course;
import com.elearning.enrollments.entity.Enrollment;
import com.elearning.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    List<Enrollment> findByStudent(User student);
    Optional<Enrollment> findByStudentAndCourse(User student, Course course);
    boolean existsByStudentAndCourse(User student, Course course);
    long countByCourse(Course course);
    List<Enrollment> findByCourse(Course course);
}
