package com.elearning.controller;

import com.elearning.entity.Lesson;
import com.elearning.service.LessonService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/courses/{courseId}/lessons")
public class LessonController {

    private final LessonService lessonService;

    public LessonController(LessonService lessonService) {
        this.lessonService = lessonService;
    }

    @GetMapping
    public ResponseEntity<?> getLessons(@PathVariable Long courseId) {
        try {
            return ResponseEntity.ok(lessonService.getLessonsByCourse(courseId));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{lessonId}")
    public ResponseEntity<?> getLesson(@PathVariable Long courseId, @PathVariable Long lessonId) {
        try {
            return ResponseEntity.ok(lessonService.getLesson(courseId, lessonId));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<?> createLesson(@PathVariable Long courseId, @RequestBody Map<String, Object> body, Authentication auth) {
        try {
            Long instructorId = (Long) auth.getPrincipal();
            String title = (String) body.get("title");
            String content = (String) body.get("content");
            Integer orderIndex = (Integer) body.get("orderIndex");
            Integer durationMinutes = (Integer) body.get("durationMinutes");
            return ResponseEntity.ok(lessonService.createLesson(courseId, title, content, orderIndex, durationMinutes, instructorId));
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{lessonId}")
    public ResponseEntity<?> deleteLesson(@PathVariable Long courseId, @PathVariable Long lessonId, Authentication auth) {
        try {
            Long instructorId = (Long) auth.getPrincipal();
            lessonService.deleteLesson(courseId, lessonId, instructorId);
            return ResponseEntity.ok(Map.of("message", "Lesson deleted"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(Map.of("error", e.getMessage()));
        }
    }
}
