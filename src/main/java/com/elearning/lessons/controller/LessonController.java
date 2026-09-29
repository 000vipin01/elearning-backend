package com.elearning.lessons.controller;

import com.elearning.lessons.dto.LessonRequest;
import com.elearning.lessons.dto.LessonResponse;
import com.elearning.lessons.service.LessonService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/courses/{courseId}/lessons")
public class LessonController {

    private final LessonService lessonService;

    public LessonController(LessonService lessonService) {
        this.lessonService = lessonService;
    }

    @GetMapping
    public ResponseEntity<List<LessonResponse>> getLessons(@PathVariable Long courseId) {
        return ResponseEntity.ok(lessonService.getLessons(courseId));
    }

    @GetMapping("/{lessonId}")
    public ResponseEntity<LessonResponse> getLesson(@PathVariable Long courseId, @PathVariable Long lessonId) {
        return ResponseEntity.ok(lessonService.getLesson(courseId, lessonId));
    }

    @PostMapping
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<LessonResponse> createLesson(@PathVariable Long courseId, @Valid @RequestBody LessonRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(lessonService.createLesson(courseId, request));
    }

    @PutMapping("/{lessonId}")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<LessonResponse> updateLesson(@PathVariable Long courseId, @PathVariable Long lessonId, @Valid @RequestBody LessonRequest request) {
        return ResponseEntity.ok(lessonService.updateLesson(courseId, lessonId, request));
    }

    @DeleteMapping("/{lessonId}")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<Void> deleteLesson(@PathVariable Long courseId, @PathVariable Long lessonId) {
        lessonService.deleteLesson(courseId, lessonId);
        return ResponseEntity.noContent().build();
    }
}
