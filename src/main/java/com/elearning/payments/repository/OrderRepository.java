package com.elearning.payments.repository;

import com.elearning.payments.entity.Order;
import com.elearning.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByStudent(User student);
    Optional<Order> findByIdempotencyKey(String idempotencyKey);
}
