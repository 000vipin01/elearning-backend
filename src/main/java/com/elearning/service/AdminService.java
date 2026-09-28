package com.elearning.service;

import com.elearning.entity.User;
import com.elearning.repository.CourseRepository;
import com.elearning.repository.EnrollmentRepository;
import com.elearning.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdminService {

    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;

    public AdminService(UserRepository userRepository, CourseRepository courseRepository,
                        EnrollmentRepository enrollmentRepository) {
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
        this.enrollmentRepository = enrollmentRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (user.getRole() == User.Role.ADMIN) {
            throw new RuntimeException("Cannot delete admin users");
        }
        userRepository.delete(user);
    }

    public Map<String, Object> updateUserRole(Long id, String role) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (user.getRole() == User.Role.ADMIN) {
            throw new RuntimeException("Cannot change admin role");
        }

        User.Role newRole;
        try {
            newRole = User.Role.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid role. Must be STUDENT or INSTRUCTOR");
        }

        // If demoting from INSTRUCTOR to STUDENT, delete all their courses
        if (user.getRole() == User.Role.INSTRUCTOR && newRole == User.Role.STUDENT) {
            List<com.elearning.entity.Course> courses = courseRepository.findByInstructor(user);
            for (com.elearning.entity.Course course : courses) {
                courseRepository.delete(course);
            }
        }

        User.Role oldRole = user.getRole();
        user.setRole(newRole);
        userRepository.save(user);

        // Return flag so frontend can prompt for course creation
        Map<String, Object> result = new HashMap<>();
        result.put("user", user);
        result.put("requiresCourseCreation", oldRole == User.Role.STUDENT && newRole == User.Role.INSTRUCTOR);
        return result;
    }

    public Map<String, Object> getPlatformStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUsers", userRepository.count());
        stats.put("totalStudents", userRepository.findAll().stream()
                .filter(u -> u.getRole() == User.Role.STUDENT).count());
        stats.put("totalInstructors", userRepository.findAll().stream()
                .filter(u -> u.getRole() == User.Role.INSTRUCTOR).count());
        stats.put("totalCourses", courseRepository.count());
        stats.put("totalEnrollments", enrollmentRepository.count());
        return stats;
    }

    public List<Map<String, Object>> getEnrolledStudents(Long instructorId) {
        User instructor = userRepository.findById(instructorId)
                .orElseThrow(() -> new RuntimeException("Instructor not found"));
        List<com.elearning.entity.Course> courses = courseRepository.findByInstructor(instructor);
        List<Map<String, Object>> result = new java.util.ArrayList<>();
        for (com.elearning.entity.Course course : courses) {
            List<com.elearning.entity.Enrollment> enrollments = enrollmentRepository.findAll().stream()
                    .filter(e -> e.getCourse().getId().equals(course.getId()))
                    .toList();
            for (com.elearning.entity.Enrollment e : enrollments) {
                Map<String, Object> studentInfo = new HashMap<>();
                studentInfo.put("studentId", e.getStudent().getId());
                studentInfo.put("studentName", e.getStudent().getName());
                studentInfo.put("studentEmail", e.getStudent().getEmail());
                studentInfo.put("courseId", course.getId());
                studentInfo.put("courseTitle", course.getTitle());
                studentInfo.put("progress", e.getProgress());
                studentInfo.put("enrolledAt", e.getEnrolledAt());
                result.add(studentInfo);
            }
        }
        return result;
    }
}
