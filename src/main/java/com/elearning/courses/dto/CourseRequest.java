package com.elearning.courses.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record CourseRequest(
    @NotBlank @Size(min = 3, max = 200) String title,
    @Size(max = 2000) String description,
    @Size(max = 100) String category,
    @NotNull @PositiveOrZero BigDecimal price,
    @Size(max = 20) String level
) {}
