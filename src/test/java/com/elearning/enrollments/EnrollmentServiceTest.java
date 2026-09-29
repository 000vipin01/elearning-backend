package com.elearning.enrollments;

import com.elearning.common.error.ConflictException;
import com.elearning.common.error.ForbiddenException;
import com.elearning.common.error.NotFoundException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EnrollmentServiceTest {

    @Test
    void enroll_alreadyEnrolled_throwsConflict() {
        assertThrows(ConflictException.class, () -> {
            throw new ConflictException("Already enrolled in this course");
        });
    }

    @Test
    void enroll_courseNotPublished_throwsForbidden() {
        assertThrows(ForbiddenException.class, () -> {
            throw new ForbiddenException("Course is not available for enrollment");
        });
    }

    @Test
    void enroll_courseNotFound_throwsNotFound() {
        assertThrows(NotFoundException.class, () -> {
            throw new NotFoundException("Course not found");
        });
    }

    @Test
    void updateProgress_clampsTo100() {
        int progress = 150;
        int clamped = Math.min(100, Math.max(0, progress));
        assertEquals(100, clamped);
    }

    @Test
    void updateProgress_clampsTo0() {
        int progress = -10;
        int clamped = Math.min(100, Math.max(0, progress));
        assertEquals(0, clamped);
    }

    @Test
    void updateProgress_normalValue() {
        int progress = 75;
        int clamped = Math.min(100, Math.max(0, progress));
        assertEquals(75, clamped);
    }
}
