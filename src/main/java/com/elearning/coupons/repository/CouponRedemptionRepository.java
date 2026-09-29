package com.elearning.coupons.repository;

import com.elearning.coupons.entity.Coupon;
import com.elearning.coupons.entity.CouponRedemption;
import com.elearning.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CouponRedemptionRepository extends JpaRepository<CouponRedemption, Long> {
    List<CouponRedemption> findByCouponAndUser(Coupon coupon, User user);
    long countByCouponAndUser(Coupon coupon, User user);
}
