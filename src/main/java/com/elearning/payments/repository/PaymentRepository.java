package com.elearning.payments.repository;

import com.elearning.payments.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByProviderPaymentId(String providerPaymentId);
    List<Payment> findByOrderId(Long orderId);
    Optional<Payment> findByIdempotencyKey(String idempotencyKey);
}
