package com.elearning.controller;

import com.elearning.dto.CourseRequest;
import com.elearning.entity.Course;
import com.elearning.service.CourseService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public List<Course> getAllCourses(@RequestParam(required = false) String search) {
        return courseService.searchCourses(search);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCourse(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(courseService.getCourse(id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<?> createCourse(@Valid @RequestBody CourseRequest request, Authentication auth) {
        try {
            Long instructorId = (Long) auth.getPrincipal();
            return ResponseEntity.ok(courseService.createCourse(request, instructorId));
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCourse(@PathVariable Long id, @Valid @RequestBody CourseRequest request, Authentication auth) {
        try {
            Long instructorId = (Long) auth.getPrincipal();
            return ResponseEntity.ok(courseService.updateCourse(id, request, instructorId));
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCourse(@PathVariable Long id, Authentication auth) {
        try {
            Long instructorId = (Long) auth.getPrincipal();
            courseService.deleteCourse(id, instructorId);
            return ResponseEntity.ok(Map.of("message", "Course deleted"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/instructor/{instructorId}")
    public List<Course> getCoursesByInstructor(@PathVariable Long instructorId) {
        return courseService.getCoursesByInstructor(instructorId);
    }
}
