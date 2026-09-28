package com.elearning.service;

import com.elearning.dto.CourseRequest;
import com.elearning.entity.Course;
import com.elearning.entity.User;
import com.elearning.repository.CourseRepository;
import com.elearning.repository.EnrollmentRepository;
import com.elearning.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final EnrollmentRepository enrollmentRepository;

    public CourseService(CourseRepository courseRepository, UserRepository userRepository, EnrollmentRepository enrollmentRepository) {
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
        this.enrollmentRepository = enrollmentRepository;
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public List<Course> searchCourses(String query) {
        if (query == null || query.isBlank()) {
            return courseRepository.findAll();
        }
        return courseRepository.findByTitleContainingIgnoreCase(query);
    }

    public Course getCourse(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found"));
    }

    public Course createCourse(CourseRequest request, Long instructorId) {
        User instructor = userRepository.findById(instructorId)
                .orElseThrow(() -> new RuntimeException("Instructor not found"));

        Course course = new Course();
        course.setTitle(request.getTitle());
        course.setDescription(request.getDescription());
        course.setCategory(request.getCategory());
        course.setInstructor(instructor);

        return courseRepository.save(course);
    }

    public Course updateCourse(Long id, CourseRequest request, Long instructorId) {
        Course course = getCourse(id);
        if (!course.getInstructor().getId().equals(instructorId)) {
            throw new RuntimeException("Not authorized to update this course");
        }

        course.setTitle(request.getTitle());
        course.setDescription(request.getDescription());
        course.setCategory(request.getCategory());

        return courseRepository.save(course);
    }

    public void deleteCourse(Long id, Long instructorId) {
        Course course = getCourse(id);
        if (!course.getInstructor().getId().equals(instructorId)) {
            throw new RuntimeException("Not authorized to delete this course");
        }

        // Check for existing enrollments
        long enrollmentCount = enrollmentRepository.countByCourse(course);
        if (enrollmentCount > 0) {
            throw new RuntimeException("Cannot delete course with " + enrollmentCount + " active enrollments");
        }

        courseRepository.delete(course);
    }

    public List<Course> getCoursesByInstructor(Long instructorId) {
        User instructor = userRepository.findById(instructorId)
                .orElseThrow(() -> new RuntimeException("Instructor not found"));
        return courseRepository.findByInstructor(instructor);
    }
}
