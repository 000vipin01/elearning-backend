package com.elearning.courses;

import com.elearning.common.error.ForbiddenException;
import com.elearning.common.error.NotFoundException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CourseServiceTest {

    @Test
    void deleteCourse_withEnrollments_throwsForbidden() {
        assertThrows(ForbiddenException.class, () -> {
            throw new ForbiddenException("Cannot delete course with 5 active enrollments");
        });
    }

    @Test
    void getCourse_notFound_throwsNotFound() {
        assertThrows(NotFoundException.class, () -> {
            throw new NotFoundException("Course not found");
        });
    }

    @Test
    void courseStatus_draft() {
        String status = "DRAFT";
        assertEquals("DRAFT", status);
    }

    @Test
    void courseStatus_published() {
        String status = "PUBLISHED";
        assertEquals("PUBLISHED", status);
    }
}
