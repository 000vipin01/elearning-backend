package com.elearning.controller;

import com.elearning.entity.Course;
import com.elearning.entity.Enrollment;
import com.elearning.entity.User;
import com.elearning.repository.CourseRepository;
import com.elearning.repository.EnrollmentRepository;
import com.elearning.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/dashboard")
public class DashboardController {

    private final EnrollmentRepository enrollmentRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    public DashboardController(EnrollmentRepository enrollmentRepository, CourseRepository courseRepository,
                               UserRepository userRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/student")
    public ResponseEntity<?> getStudentDashboard(Authentication auth) {
        try {
            Long studentId = (Long) auth.getPrincipal();
            User student = userRepository.findById(studentId)
                    .orElseThrow(() -> new RuntimeException("Student not found"));

            List<Enrollment> enrollments = enrollmentRepository.findByStudent(student);

            int totalEnrolled = enrollments.size();
            int completed = (int) enrollments.stream()
                    .filter(e -> e.getProgress() != null && e.getProgress() == 100)
                    .count();
            int inProgress = totalEnrolled - completed;

            Map<String, Object> result = new HashMap<>();
            result.put("totalEnrolled", totalEnrolled);
            result.put("completed", completed);
            result.put("inProgress", inProgress);
            result.put("enrollments", enrollments);

            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/instructor")
    public ResponseEntity<?> getInstructorDashboard(Authentication auth) {
        try {
            Long instructorId = (Long) auth.getPrincipal();
            User instructor = userRepository.findById(instructorId)
                    .orElseThrow(() -> new RuntimeException("Instructor not found"));

            List<Course> courses = courseRepository.findByInstructor(instructor);
            List<Object[]> countResults = enrollmentRepository.countStudentsByInstructor(instructorId);

            Map<Long, Long> studentCountMap = new HashMap<>();
            for (Object[] row : countResults) {
                studentCountMap.put((Long) row[0], (Long) row[1]);
            }

            int totalCourses = courses.size();
            int totalStudents = (int) countResults.stream()
                    .mapToLong(row -> (Long) row[1])
                    .sum();

            // Attach studentCount to each course for frontend use
            List<Map<String, Object>> courseData = new java.util.ArrayList<>();
            for (Course c : courses) {
                Map<String, Object> courseMap = new HashMap<>();
                courseMap.put("id", c.getId());
                courseMap.put("title", c.getTitle());
                courseMap.put("description", c.getDescription());
                courseMap.put("category", c.getCategory());
                courseMap.put("instructor", c.getInstructor());
                courseMap.put("createdAt", c.getCreatedAt());
                courseMap.put("studentCount", studentCountMap.getOrDefault(c.getId(), 0L));
                courseData.add(courseMap);
            }

            Map<String, Object> result = new HashMap<>();
            result.put("totalCourses", totalCourses);
            result.put("totalStudents", totalStudents);
            result.put("courses", courseData);

            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(Map.of("error", e.getMessage()));
        }
    }
}
