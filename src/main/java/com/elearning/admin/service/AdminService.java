package com.elearning.admin.service;

import com.elearning.admin.dto.PlatformStats;
import com.elearning.ads.repository.AdRepository;
import com.elearning.common.audit.AuditRepository;
import com.elearning.courses.repository.CourseRepository;
import com.elearning.enrollments.repository.EnrollmentRepository;
import com.elearning.lessons.repository.LessonRepository;
import com.elearning.notifications.repository.NotificationRepository;
import com.elearning.offers.repository.OfferRepository;
import com.elearning.payments.repository.OrderRepository;
import com.elearning.payments.repository.PaymentRepository;
import com.elearning.payments.repository.RefundRepository;
import com.elearning.quizzes.repository.QuizRepository;
import com.elearning.users.entity.User;
import com.elearning.users.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class AdminService {

    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final LessonRepository lessonRepository;
    private final QuizRepository quizRepository;
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final RefundRepository refundRepository;
    private final OfferRepository offerRepository;
    private final AdRepository adRepository;
    private final NotificationRepository notificationRepository;
    private final AuditRepository auditRepository;

    public AdminService(UserRepository userRepository, CourseRepository courseRepository,
                        EnrollmentRepository enrollmentRepository, LessonRepository lessonRepository,
                        QuizRepository quizRepository, OrderRepository orderRepository,
                        PaymentRepository paymentRepository, RefundRepository refundRepository,
                        OfferRepository offerRepository, AdRepository adRepository,
                        NotificationRepository notificationRepository, AuditRepository auditRepository) {
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.lessonRepository = lessonRepository;
        this.quizRepository = quizRepository;
        this.orderRepository = orderRepository;
        this.paymentRepository = paymentRepository;
        this.refundRepository = refundRepository;
        this.offerRepository = offerRepository;
        this.adRepository = adRepository;
        this.notificationRepository = notificationRepository;
        this.auditRepository = auditRepository;
    }

    public PlatformStats getPlatformStats() {
        long totalUsers = userRepository.count();
        long totalStudents = userRepository.findAll().stream().filter(u -> u.getRole() == User.Role.STUDENT).count();
        long totalInstructors = userRepository.findAll().stream().filter(u -> u.getRole() == User.Role.INSTRUCTOR).count();
        long totalCourses = courseRepository.count();
        long publishedCourses = courseRepository.findByStatus("PUBLISHED").size();
        long totalEnrollments = enrollmentRepository.count();
        long totalLessons = lessonRepository.count();
        long totalQuizzes = quizRepository.count();
        long totalOrders = orderRepository.count();
        long totalPayments = paymentRepository.count();
        BigDecimal totalRevenue = paymentRepository.findAll().stream()
            .filter(p -> "SUCCESS".equals(p.getStatus()))
            .map(p -> p.getAmount())
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        long totalRefunds = refundRepository.count();
        BigDecimal totalRefundAmount = refundRepository.findAll().stream()
            .filter(r -> "COMPLETED".equals(r.getStatus()))
            .map(r -> r.getAmount())
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        long totalOffers = offerRepository.count();
        long totalAds = adRepository.count();
        long totalNotifications = notificationRepository.count();

        return new PlatformStats(totalUsers, totalStudents, totalInstructors, totalCourses,
            publishedCourses, totalEnrollments, totalLessons, totalQuizzes, totalOrders,
            totalPayments, totalRevenue, totalRefunds, totalRefundAmount, 0,
            totalOffers, totalAds, totalNotifications);
    }
}
