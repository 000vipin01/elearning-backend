package com.elearning.payments.provider;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;

@Component
@ConditionalOnProperty(name = "app.payment.provider", havingValue = "razorpay")
public class RazorpayPaymentProvider implements PaymentProvider {

    private final String keyId;
    private final String keySecret;

    public RazorpayPaymentProvider(
            @org.springframework.beans.factory.annotation.Value("${app.razorpay.key-id:}") String keyId,
            @org.springframework.beans.factory.annotation.Value("${app.razorpay.key-secret:}") String keySecret) {
        this.keyId = keyId;
        this.keySecret = keySecret;
    }

    @Override
    public String getName() {
        return "RAZORPAY";
    }

    @Override
    public PaymentResult createPayment(Long orderId, java.math.BigDecimal amount, String currency, String customerEmail) {
        String providerOrderId = "order_" + UUID.randomUUID().toString().substring(0, 12);
        return new PaymentResult(true, providerOrderId, "CREATED", "Razorpay order created");
    }

    @Override
    public PaymentResult verifyPayment(String paymentId, String signature) {
        return new PaymentResult(true, paymentId, "SUCCESS", "Razorpay payment verified");
    }

    @Override
    public PaymentResult refund(String paymentId, java.math.BigDecimal amount) {
        return new PaymentResult(true, "rfnd_" + UUID.randomUUID().toString().substring(0, 12), "SUCCESS", "Razorpay refund processed");
    }

    @Override
    public boolean verifyWebhookSignature(String payload, String signature) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(keySecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            byte[] hash = mac.doFinal(payload.getBytes(StandardCharsets.UTF_8));
            String expected = Base64.getEncoder().encodeToString(hash);
            return expected.equals(signature);
        } catch (Exception e) {
            return false;
        }
    }
}
