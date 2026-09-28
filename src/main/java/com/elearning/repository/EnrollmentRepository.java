package com.elearning.repository;

import com.elearning.entity.Course;
import com.elearning.entity.Enrollment;
import com.elearning.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    List<Enrollment> findByStudent(User student);
    Optional<Enrollment> findByStudentAndCourse(User student, Course course);
    boolean existsByStudentAndCourse(User student, Course course);
    long countByCourse(Course course);

    @Query("SELECT c.id AS courseId, COUNT(e) AS studentCount FROM Course c LEFT JOIN Enrollment e ON e.course.id = c.id WHERE c.instructor.id = :instructorId GROUP BY c.id")
    List<Object[]> countStudentsByInstructor(@Param("instructorId") Long instructorId);
}
