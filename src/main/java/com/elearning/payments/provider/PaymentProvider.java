package com.elearning.payments.provider;

import java.math.BigDecimal;

public interface PaymentProvider {
    String getName();
    PaymentResult createPayment(Long orderId, BigDecimal amount, String currency, String customerEmail);
    PaymentResult verifyPayment(String paymentId, String signature);
    PaymentResult refund(String paymentId, BigDecimal amount);
    boolean verifyWebhookSignature(String payload, String signature);

    record PaymentResult(boolean success, String paymentId, String status, String message) {}
}
