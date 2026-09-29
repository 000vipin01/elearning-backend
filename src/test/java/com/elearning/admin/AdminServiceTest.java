package com.elearning.admin;

import com.elearning.admin.dto.PlatformStats;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class AdminServiceTest {

    @Test
    void platformStats_allZeros() {
        PlatformStats stats = new PlatformStats(0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            BigDecimal.ZERO, 0, BigDecimal.ZERO, 0, 0, 0, 0);

        assertEquals(0, stats.totalUsers());
        assertEquals(0, stats.totalStudents());
        assertEquals(0, stats.totalInstructors());
        assertEquals(0, stats.totalCourses());
        assertEquals(0, stats.totalEnrollments());
        assertEquals(BigDecimal.ZERO, stats.totalRevenue());
    }

    @Test
    void platformStats_withData() {
        PlatformStats stats = new PlatformStats(100, 80, 15, 10, 8, 200, 50, 12, 150, 140,
            BigDecimal.valueOf(50000), 5, BigDecimal.valueOf(2000), 10, 3, 5, 500);

        assertEquals(100, stats.totalUsers());
        assertEquals(80, stats.totalStudents());
        assertEquals(15, stats.totalInstructors());
        assertEquals(10, stats.totalCourses());
        assertEquals(8, stats.publishedCourses());
        assertEquals(200, stats.totalEnrollments());
        assertEquals(0, stats.totalRevenue().compareTo(BigDecimal.valueOf(50000)));
        assertEquals(5, stats.totalRefunds());
    }

    @Test
    void platformStats_revenueCalculation() {
        BigDecimal revenue = BigDecimal.valueOf(10000).add(BigDecimal.valueOf(20000));
        assertEquals(0, revenue.compareTo(BigDecimal.valueOf(30000)));
    }
}
