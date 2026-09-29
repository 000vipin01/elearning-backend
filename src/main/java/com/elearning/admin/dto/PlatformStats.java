package com.elearning.admin.dto;

import java.math.BigDecimal;

public record PlatformStats(
    long totalUsers,
    long totalStudents,
    long totalInstructors,
    long totalCourses,
    long publishedCourses,
    long totalEnrollments,
    long totalLessons,
    long totalQuizzes,
    long totalOrders,
    long totalPayments,
    BigDecimal totalRevenue,
    long totalRefunds,
    BigDecimal totalRefundAmount,
    long totalCoupons,
    long totalOffers,
    long totalAds,
    long totalNotifications
) {}
