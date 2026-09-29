package com.elearning.payments.controller;

import com.elearning.common.security.AuthenticatedUser;
import com.elearning.common.security.SecurityUtils;
import com.elearning.payments.dto.*;
import com.elearning.payments.entity.Order;
import com.elearning.payments.entity.Payment;
import com.elearning.payments.entity.Refund;
import com.elearning.payments.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/orders")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody CreateOrderRequest request) {
        Order order = paymentService.createOrder(request.courseId(), request.couponCode(), request.idempotencyKey());
        return ResponseEntity.status(HttpStatus.CREATED).body(toOrderResponse(order));
    }

    @PostMapping("/orders/{orderId}/pay")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<PaymentResponse> processPayment(@PathVariable Long orderId) {
        Payment payment = paymentService.processPayment(orderId);
        return ResponseEntity.ok(toPaymentResponse(payment));
    }

    @PostMapping("/verify")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<PaymentResponse> verifyPayment(@RequestBody PaymentVerificationRequest request) {
        Payment payment = paymentService.verifyPayment(request.paymentId(), request.signature());
        return ResponseEntity.ok(toPaymentResponse(payment));
    }

    @PostMapping("/refunds")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Refund> refundPayment(@Valid @RequestBody RefundRequest request) {
        return ResponseEntity.ok(paymentService.refundPayment(request.paymentId(), request.amount(), request.reason()));
    }

    @GetMapping("/orders/my")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<OrderResponse>> getMyOrders() {
        return ResponseEntity.ok(paymentService.getMyOrders().stream().map(this::toOrderResponse).toList());
    }

    @GetMapping("/orders/{orderId}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<PaymentResponse>> getOrderPayments(@PathVariable Long orderId) {
        return ResponseEntity.ok(paymentService.getOrderPayments(orderId).stream().map(this::toPaymentResponse).toList());
    }

    private OrderResponse toOrderResponse(Order order) {
        return new OrderResponse(order.getId(), order.getStudent().getId(), order.getStatus(),
            order.getTotalAmount(), order.getDiscountAmount(), order.getFinalAmount(),
            order.getIdempotencyKey(), order.getCreatedAt());
    }

    private PaymentResponse toPaymentResponse(Payment payment) {
        return new PaymentResponse(payment.getId(), payment.getOrder().getId(), payment.getProvider(),
            payment.getProviderPaymentId(), payment.getAmount(), payment.getCurrency(),
            payment.getStatus(), payment.getSignatureVerified(), payment.getPaidAt(), payment.getCreatedAt());
    }

    public record PaymentVerificationRequest(Long paymentId, String signature) {}
}
