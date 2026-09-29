package com.elearning.lessons.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record LessonRequest(
    @NotBlank @Size(min = 3, max = 200) String title,
    @Size(max = 5000) String content,
    @NotNull Integer orderIndex,
    Integer durationMinutes,
    Boolean isFreePreview,
    String mediaType,
    String mediaPath,
    Integer mediaDuration,
    Long mediaSize,
    String mediaMime
) {}
