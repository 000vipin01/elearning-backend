package com.elearning.admin.controller;

import com.elearning.admin.dto.PlatformStats;
import com.elearning.admin.service.AdminService;
import com.elearning.common.audit.AuditLog;
import com.elearning.common.audit.AuditRepository;
import com.elearning.courses.dto.CourseResponse;
import com.elearning.courses.service.CourseService;
import com.elearning.users.dto.RoleUpdateRequest;
import com.elearning.users.dto.UserResponse;
import com.elearning.users.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    private final AdminService adminService;
    private final UserService userService;
    private final CourseService courseService;
    private final AuditRepository auditRepository;

    public AdminController(AdminService adminService, UserService userService,
                          CourseService courseService, AuditRepository auditRepository) {
        this.adminService = adminService;
        this.userService = userService;
        this.courseService = courseService;
        this.auditRepository = auditRepository;
    }

    @GetMapping("/stats")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PlatformStats> getPlatformStats() {
        return ResponseEntity.ok(adminService.getPlatformStats());
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PutMapping("/users/{id}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> updateUserRole(@PathVariable Long id, @Valid @RequestBody RoleUpdateRequest request) {
        return ResponseEntity.ok(userService.updateRole(id, request));
    }

    @DeleteMapping("/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(Map.of("message", "User deleted"));
    }

    @GetMapping("/courses/pending")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<CourseResponse>> getPendingCourses() {
        var allCourses = courseService.listCourses(null, null, 0, 100, "createdAt", "desc");
        var pending = allCourses.content().stream()
            .filter(c -> "DRAFT".equals(c.status()) || "REVIEW".equals(c.status()))
            .toList();
        return ResponseEntity.ok(pending);
    }

    @PostMapping("/courses/{id}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CourseResponse> approveCourse(@PathVariable Long id) {
        return ResponseEntity.ok(courseService.publishCourse(id));
    }

    @PostMapping("/courses/{id}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CourseResponse> rejectCourse(@PathVariable Long id) {
        return ResponseEntity.ok(courseService.rejectCourse(id));
    }

    @GetMapping("/audit-log")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AuditLog>> getAuditLog() {
        return ResponseEntity.ok(auditRepository.findAll());
    }
}
