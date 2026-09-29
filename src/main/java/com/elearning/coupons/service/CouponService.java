package com.elearning.coupons.service;

import com.elearning.common.error.ForbiddenException;
import com.elearning.common.error.NotFoundException;
import com.elearning.common.security.AuthenticatedUser;
import com.elearning.common.security.SecurityUtils;
import com.elearning.coupons.dto.CouponRequest;
import com.elearning.coupons.dto.CouponResponse;
import com.elearning.coupons.entity.Coupon;
import com.elearning.coupons.repository.CouponRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CouponService {

    private final CouponRepository couponRepository;

    public CouponService(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    @Transactional
    public CouponResponse createCoupon(CouponRequest request) {
        AuthenticatedUser currentUser = SecurityUtils.getCurrentUser();

        Coupon coupon = new Coupon();
        coupon.setCode(request.code());
        coupon.setDescription(request.description());
        coupon.setDiscountType(request.discountType());
        coupon.setDiscountValue(request.discountValue());
        coupon.setMaxUses(request.maxUses() != null ? request.maxUses() : 0);
        coupon.setMaxUsesPerUser(request.maxUsesPerUser() != null ? request.maxUsesPerUser() : 1);
        coupon.setMinOrderAmount(request.minOrderAmount() != null ? request.minOrderAmount() : java.math.BigDecimal.ZERO);
        coupon.setIsActive(true);

        couponRepository.save(coupon);
        return toResponse(coupon);
    }

    public List<CouponResponse> getAllCoupons() {
        return couponRepository.findAll().stream().map(this::toResponse).toList();
    }

    public CouponResponse getCoupon(Long id) {
        return toResponse(getCouponEntity(id));
    }

    @Transactional
    public CouponResponse updateCoupon(Long id, CouponRequest request) {
        Coupon coupon = getCouponEntity(id);
        coupon.setCode(request.code());
        coupon.setDescription(request.description());
        coupon.setDiscountType(request.discountType());
        coupon.setDiscountValue(request.discountValue());
        coupon.setMaxUses(request.maxUses() != null ? request.maxUses() : 0);
        coupon.setMaxUsesPerUser(request.maxUsesPerUser() != null ? request.maxUsesPerUser() : 1);
        coupon.setMinOrderAmount(request.minOrderAmount() != null ? request.minOrderAmount() : java.math.BigDecimal.ZERO);
        couponRepository.save(coupon);
        return toResponse(coupon);
    }

    @Transactional
    public void deleteCoupon(Long id) {
        Coupon coupon = getCouponEntity(id);
        couponRepository.delete(coupon);
    }

    private Coupon getCouponEntity(Long id) {
        return couponRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Coupon not found"));
    }

    private CouponResponse toResponse(Coupon coupon) {
        return new CouponResponse(coupon.getId(), coupon.getCode(), coupon.getDescription(),
            coupon.getDiscountType(), coupon.getDiscountValue(), coupon.getMaxUses(), coupon.getUsedCount(),
            coupon.getMaxUsesPerUser(), coupon.getMinOrderAmount(), coupon.getStartsAt(), coupon.getExpiresAt(),
            coupon.getIsActive(), coupon.getCreatedAt());
    }
}
