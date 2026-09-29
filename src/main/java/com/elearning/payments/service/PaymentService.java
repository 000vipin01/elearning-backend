package com.elearning.payments.service;

import com.elearning.common.error.BadRequestException;
import com.elearning.common.error.ConflictException;
import com.elearning.common.error.NotFoundException;
import com.elearning.common.security.AuthenticatedUser;
import com.elearning.common.security.SecurityUtils;
import com.elearning.courses.entity.Course;
import com.elearning.courses.repository.CourseRepository;
import com.elearning.enrollments.entity.Enrollment;
import com.elearning.enrollments.repository.EnrollmentRepository;
import com.elearning.payments.entity.Order;
import com.elearning.payments.entity.OrderItem;
import com.elearning.payments.entity.Payment;
import com.elearning.payments.entity.Refund;
import com.elearning.payments.provider.PaymentProvider;
import com.elearning.payments.repository.OrderRepository;
import com.elearning.payments.repository.PaymentRepository;
import com.elearning.payments.repository.RefundRepository;
import com.elearning.users.entity.User;
import com.elearning.users.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class PaymentService {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final RefundRepository refundRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final PaymentProvider paymentProvider;
    private final PricingService pricingService;

    public PaymentService(OrderRepository orderRepository, PaymentRepository paymentRepository,
                          RefundRepository refundRepository, CourseRepository courseRepository,
                          UserRepository userRepository, EnrollmentRepository enrollmentRepository,
                          PaymentProvider paymentProvider, PricingService pricingService) {
        this.orderRepository = orderRepository;
        this.paymentRepository = paymentRepository;
        this.refundRepository = refundRepository;
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.paymentProvider = paymentProvider;
        this.pricingService = pricingService;
    }

    @Transactional
    public Order createOrder(Long courseId, String couponCode, String idempotencyKey) {
        AuthenticatedUser currentUser = SecurityUtils.getCurrentUser();
        User student = userRepository.findById(currentUser.id())
            .orElseThrow(() -> new NotFoundException("User not found"));
        Course course = courseRepository.findById(courseId)
            .orElseThrow(() -> new NotFoundException("Course not found"));

        if (!"PUBLISHED".equals(course.getStatus())) {
            throw new BadRequestException("Course is not available for purchase");
        }

        if (enrollmentRepository.existsByStudentAndCourse(student, course)) {
            throw new ConflictException("Already enrolled in this course");
        }

        if (idempotencyKey != null) {
            var existing = orderRepository.findByIdempotencyKey(idempotencyKey);
            if (existing.isPresent()) return existing.get();
        }

        PricingService.PricingResult pricing = pricingService.calculatePrice(course, couponCode);

        Order order = new Order();
        order.setStudent(student);
        order.setStatus("PENDING");
        order.setTotalAmount(pricing.originalPrice());
        order.setDiscountAmount(pricing.discount());
        order.setFinalAmount(pricing.finalPrice());
        order.setIdempotencyKey(idempotencyKey != null ? idempotencyKey : UUID.randomUUID().toString());

        OrderItem item = new OrderItem();
        item.setCourse(course);
        item.setOriginalPrice(pricing.originalPrice());
        item.setDiscountAmount(pricing.discount());
        item.setFinalPrice(pricing.finalPrice());

        order = orderRepository.save(order);
        item.setOrder(order);
        orderRepository.save(order);

        return order;
    }

    @Transactional
    public Payment processPayment(Long orderId) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new NotFoundException("Order not found"));

        if (!"PENDING".equals(order.getStatus())) {
            throw new BadRequestException("Order is not in PENDING state");
        }

        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setProvider(paymentProvider.getName());
        payment.setAmount(order.getFinalAmount());
        payment.setCurrency("INR");
        payment.setStatus("PENDING");
        payment.setIdempotencyKey(UUID.randomUUID().toString());

        payment = paymentRepository.save(payment);

        PaymentProvider.PaymentResult result = paymentProvider.createPayment(
            order.getId(), order.getFinalAmount(), "INR", order.getStudent().getEmail());

        if (result.success()) {
            payment.setProviderPaymentId(result.paymentId());
            payment.setStatus("SUCCESS");
            payment.setSignatureVerified(true);
            payment.setPaidAt(LocalDateTime.now());
            order.setStatus("COMPLETED");
            orderRepository.save(order);

            // Create enrollment on successful payment
            createEnrollment(order);
        } else {
            payment.setStatus("FAILED");
            payment.setFailureReason(result.message());
            order.setStatus("FAILED");
            orderRepository.save(order);
        }

        paymentRepository.save(payment);
        return payment;
    }

    @Transactional
    public Payment verifyPayment(Long paymentId, String signature) {
        Payment payment = paymentRepository.findById(paymentId)
            .orElseThrow(() -> new NotFoundException("Payment not found"));

        if ("SUCCESS".equals(payment.getStatus())) {
            return payment;
        }

        PaymentProvider.PaymentResult result = paymentProvider.verifyPayment(
            payment.getProviderPaymentId(), signature);

        if (result.success()) {
            payment.setStatus("SUCCESS");
            payment.setSignatureVerified(true);
            payment.setPaidAt(LocalDateTime.now());
            paymentRepository.save(payment);

            Order order = payment.getOrder();
            order.setStatus("COMPLETED");
            orderRepository.save(order);

            createEnrollment(order);
        }

        return payment;
    }

    @Transactional
    public Refund refundPayment(Long paymentId, BigDecimal amount, String reason) {
        Payment payment = paymentRepository.findById(paymentId)
            .orElseThrow(() -> new NotFoundException("Payment not found"));

        if (!"SUCCESS".equals(payment.getStatus())) {
            throw new BadRequestException("Payment is not successful");
        }

        Refund refund = new Refund();
        refund.setPayment(payment);
        refund.setOrder(payment.getOrder());
        refund.setAmount(amount);
        refund.setReason(reason);
        refund.setStatus("PENDING");

        refund = refundRepository.save(refund);

        PaymentProvider.PaymentResult result = paymentProvider.refund(payment.getProviderPaymentId(), amount);

        if (result.success()) {
            refund.setStatus("COMPLETED");
            refund.setProviderRefundId(result.paymentId());
            refund.setProcessedAt(LocalDateTime.now());
        } else {
            refund.setStatus("FAILED");
        }

        refundRepository.save(refund);
        return refund;
    }

    public List<Order> getMyOrders() {
        AuthenticatedUser currentUser = SecurityUtils.getCurrentUser();
        User student = userRepository.findById(currentUser.id())
            .orElseThrow(() -> new NotFoundException("User not found"));
        return orderRepository.findByStudent(student);
    }

    public List<Payment> getOrderPayments(Long orderId) {
        return paymentRepository.findByOrderId(orderId);
    }

    private void createEnrollment(Order order) {
        User student = order.getStudent();
        Course course = order.getOrderItems().iterator().next().getCourse();

        if (enrollmentRepository.existsByStudentAndCourse(student, course)) {
            return;
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setProgress(0);
        enrollment.setOrderId(order.getId());
        enrollment.setPricePaid(order.getFinalAmount());
        enrollmentRepository.save(enrollment);
    }
}
