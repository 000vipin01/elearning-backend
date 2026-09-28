package com.elearning.controller;

import com.elearning.entity.Enrollment;
import com.elearning.service.EnrollmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/enrollments")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @PostMapping("/{courseId}")
    public ResponseEntity<?> enroll(@PathVariable Long courseId, Authentication auth) {
        try {
            Long studentId = (Long) auth.getPrincipal();
            return ResponseEntity.ok(enrollmentService.enroll(studentId, courseId));
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/my")
    public ResponseEntity<?> getMyEnrollments(Authentication auth) {
        try {
            Long studentId = (Long) auth.getPrincipal();
            List<Enrollment> enrollments = enrollmentService.getStudentEnrollments(studentId);
            return ResponseEntity.ok(enrollments);
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<?> getEnrollmentDetails(@PathVariable Long courseId, Authentication auth) {
        try {
            Long studentId = (Long) auth.getPrincipal();
            return ResponseEntity.ok(enrollmentService.getEnrollmentDetails(studentId, courseId));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{courseId}/progress")
    public ResponseEntity<?> updateProgress(@PathVariable Long courseId, @RequestBody Map<String, Integer> body, Authentication auth) {
        try {
            Long studentId = (Long) auth.getPrincipal();
            Integer progress = body.get("progress");
            return ResponseEntity.ok(enrollmentService.updateProgress(studentId, courseId, progress));
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(Map.of("error", e.getMessage()));
        }
    }
}
