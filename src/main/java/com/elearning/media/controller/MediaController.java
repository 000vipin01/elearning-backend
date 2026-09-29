package com.elearning.media.controller;

import com.elearning.common.error.ForbiddenException;
import com.elearning.common.error.NotFoundException;
import com.elearning.lessons.entity.Lesson;
import com.elearning.lessons.repository.LessonRepository;
import com.elearning.media.service.MediaService;
import com.elearning.media.storage.MediaStorage;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@RestController
@RequestMapping("/api/v1")
public class MediaController {

    private final MediaService mediaService;
    private final MediaStorage mediaStorage;
    private final LessonRepository lessonRepository;

    public MediaController(MediaService mediaService, MediaStorage mediaStorage, LessonRepository lessonRepository) {
        this.mediaService = mediaService;
        this.mediaStorage = mediaStorage;
        this.lessonRepository = lessonRepository;
    }

    @GetMapping("/lessons/{lessonId}/stream-token")
    public ResponseEntity<java.util.Map<String, String>> getStreamToken(@PathVariable Long lessonId) {
        String token = mediaService.issueStreamToken(lessonId);
        return ResponseEntity.ok(java.util.Map.of("token", token, "expiresIn", "300"));
    }

    @GetMapping("/media/stream")
    public ResponseEntity<StreamingResponseBody> streamMedia(
            @RequestParam String token,
            @RequestParam Long lessonId,
            @RequestHeader(value = "Range", required = false) String rangeHeader) {

        if (!mediaService.validateStreamAccess(token, lessonId)) {
            throw new ForbiddenException("Invalid or expired stream token");
        }

        Lesson lesson = lessonRepository.findById(lessonId)
            .orElseThrow(() -> new NotFoundException("Lesson not found"));

        if (lesson.getMediaPath() == null) {
            throw new NotFoundException("No media for this lesson");
        }

        Resource resource = mediaStorage.load(lesson.getMediaPath());
        String contentType = lesson.getMediaMime() != null ? lesson.getMediaMime() : "video/mp4";

        try {
            long contentLength = resource.contentLength();

            if (rangeHeader != null && rangeHeader.startsWith("bytes=")) {
                String[] parts = rangeHeader.substring(6).split("-");
                long start = Long.parseLong(parts[0]);
                long end = parts.length > 1 && !parts[1].isEmpty() ? Long.parseLong(parts[1]) : contentLength - 1;
                end = Math.min(end, contentLength - 1);

                if (start > end || start >= contentLength) {
                    return ResponseEntity.status(HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE)
                        .header("Content-Range", "bytes */" + contentLength)
                        .build();
                }

                long rangeLength = end - start + 1;
                long finalStart = start;
                long finalEnd = end;

                StreamingResponseBody body = outputStream -> {
                    try (InputStream is = resource.getInputStream()) {
                        is.skip(finalStart);
                        byte[] buffer = new byte[8192];
                        long remaining = rangeLength;
                        while (remaining > 0) {
                            int read = is.read(buffer, 0, (int) Math.min(buffer.length, remaining));
                            if (read == -1) break;
                            outputStream.write(buffer, 0, read);
                            remaining -= read;
                        }
                    }
                };

                return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                    .header("Content-Type", contentType)
                    .header("Content-Range", "bytes " + start + "-" + end + "/" + contentLength)
                    .header("Accept-Ranges", "bytes")
                    .header("Content-Length", String.valueOf(rangeLength))
                    .body(body);
            }

            StreamingResponseBody body = outputStream -> {
                try (InputStream is = resource.getInputStream()) {
                    byte[] buffer = new byte[8192];
                    int read;
                    while ((read = is.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, read);
                    }
                }
            };

            return ResponseEntity.ok()
                .header("Content-Type", contentType)
                .header("Accept-Ranges", "bytes")
                .header("Content-Length", String.valueOf(contentLength))
                .body(body);

        } catch (IOException e) {
            throw new RuntimeException("Failed to read media resource", e);
        }
    }
}
