package com.elearning.enrollments.controller;

import com.elearning.enrollments.dto.EnrollmentResponse;
import com.elearning.enrollments.service.EnrollmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<EnrollmentResponse> enroll(@PathVariable Long courseId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(enrollmentService.enroll(courseId));
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<EnrollmentResponse>> getMyEnrollments() {
        return ResponseEntity.ok(enrollmentService.getMyEnrollments());
    }

    @GetMapping("/{courseId}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<EnrollmentResponse> getEnrollment(@PathVariable Long courseId) {
        return ResponseEntity.ok(enrollmentService.getEnrollment(courseId));
    }

    @PutMapping("/{courseId}/progress")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<EnrollmentResponse> updateProgress(@PathVariable Long courseId, @RequestBody Map<String, Integer> body) {
        return ResponseEntity.ok(enrollmentService.updateProgress(courseId, body.get("progress")));
    }

    @GetMapping("/course/{courseId}")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<List<EnrollmentResponse>> getCourseEnrollments(@PathVariable Long courseId) {
        return ResponseEntity.ok(enrollmentService.getCourseEnrollments(courseId));
    }
}
