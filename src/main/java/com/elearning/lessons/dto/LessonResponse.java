package com.elearning.lessons.dto;

public record LessonResponse(
    Long id,
    Long courseId,
    String title,
    String content,
    Integer orderIndex,
    Integer durationMinutes,
    Boolean isFreePreview,
    String mediaType,
    String mediaPath,
    Integer mediaDuration,
    Long mediaSize,
    String mediaMime
) {}
