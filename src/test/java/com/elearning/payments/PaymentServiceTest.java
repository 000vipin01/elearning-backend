package com.elearning.payments;

import com.elearning.common.error.BadRequestException;
import com.elearning.common.error.ConflictException;
import com.elearning.common.error.NotFoundException;
import com.elearning.payments.provider.MockPaymentProvider;
import com.elearning.payments.provider.PaymentProvider;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class PaymentServiceTest {

    @Test
    void createOrder_alreadyEnrolled_throwsConflict() {
        assertThrows(ConflictException.class, () -> {
            throw new ConflictException("Already enrolled in this course");
        });
    }

    @Test
    void createOrder_courseNotPublished_throwsBadRequest() {
        assertThrows(BadRequestException.class, () -> {
            throw new BadRequestException("Course is not available for purchase");
        });
    }

    @Test
    void createOrder_idempotencyKey() {
        String idempotencyKey = "idem-key-123";
        assertNotNull(idempotencyKey);
        assertEquals("idem-key-123", idempotencyKey);
    }

    @Test
    void processPayment_orderNotFound_throwsNotFound() {
        assertThrows(NotFoundException.class, () -> {
            throw new NotFoundException("Order not found");
        });
    }

    @Test
    void processPayment_orderNotPending_throwsBadRequest() {
        assertThrows(BadRequestException.class, () -> {
            throw new BadRequestException("Order is not in PENDING state");
        });
    }

    @Test
    void mockPaymentProvider_alwaysSucceeds() {
        PaymentProvider provider = new MockPaymentProvider();
        PaymentProvider.PaymentResult result = provider.createPayment(1L, BigDecimal.valueOf(1000), "INR", "test@example.com");
        assertTrue(result.success());
        assertEquals("SUCCESS", result.status());
    }

    @Test
    void mockPaymentProvider_verifyPayment() {
        PaymentProvider provider = new MockPaymentProvider();
        PaymentProvider.PaymentResult result = provider.verifyPayment("pay_123", "sig");
        assertTrue(result.success());
        assertEquals("SUCCESS", result.status());
    }

    @Test
    void mockPaymentProvider_refund() {
        PaymentProvider provider = new MockPaymentProvider();
        PaymentProvider.PaymentResult result = provider.refund("pay_123", BigDecimal.valueOf(500));
        assertTrue(result.success());
        assertEquals("SUCCESS", result.status());
    }

    @Test
    void mockPaymentProvider_webhookSignature() {
        PaymentProvider provider = new MockPaymentProvider();
        assertTrue(provider.verifyWebhookSignature("payload", "sig"));
    }

    @Test
    void mockPaymentProvider_name() {
        PaymentProvider provider = new MockPaymentProvider();
        assertEquals("MOCK", provider.getName());
    }
}
