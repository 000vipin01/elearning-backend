package com.elearning.payments.provider;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

@Component
@ConditionalOnProperty(name = "app.payment.provider", havingValue = "mock", matchIfMissing = true)
public class MockPaymentProvider implements PaymentProvider {

    @Override
    public String getName() {
        return "MOCK";
    }

    @Override
    public PaymentResult createPayment(Long orderId, BigDecimal amount, String currency, String customerEmail) {
        String paymentId = "mock_pay_" + UUID.randomUUID().toString().substring(0, 8);
        return new PaymentResult(true, paymentId, "SUCCESS", "Mock payment successful");
    }

    @Override
    public PaymentResult verifyPayment(String paymentId, String signature) {
        return new PaymentResult(true, paymentId, "SUCCESS", "Mock payment verified");
    }

    @Override
    public PaymentResult refund(String paymentId, BigDecimal amount) {
        return new PaymentResult(true, "mock_refund_" + UUID.randomUUID().toString().substring(0, 8), "SUCCESS", "Mock refund successful");
    }

    @Override
    public boolean verifyWebhookSignature(String payload, String signature) {
        return true;
    }
}
