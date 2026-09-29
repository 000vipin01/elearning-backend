package com.elearning.payments.service;

import com.elearning.common.error.BadRequestException;
import com.elearning.common.error.NotFoundException;
import com.elearning.coupons.entity.Coupon;
import com.elearning.coupons.repository.CouponRepository;
import com.elearning.courses.entity.Course;
import com.elearning.offers.entity.Offer;
import com.elearning.offers.repository.OfferRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PricingService {

    private final CouponRepository couponRepository;
    private final OfferRepository offerRepository;

    public PricingService(CouponRepository couponRepository, OfferRepository offerRepository) {
        this.couponRepository = couponRepository;
        this.offerRepository = offerRepository;
    }

    public PricingResult calculatePrice(Course course, String couponCode) {
        BigDecimal originalPrice = course.getPrice();
        BigDecimal discount = BigDecimal.ZERO;
        String discountDescription = null;

        // Apply best offer (if any active)
        List<Offer> activeOffers = offerRepository.findByCourseId(course.getId()).stream()
            .filter(o -> "APPROVED".equals(o.getStatus()))
            .filter(o -> o.getStartsAt().isBefore(LocalDateTime.now()))
            .filter(o -> o.getEndsAt().isAfter(LocalDateTime.now()))
            .toList();

        if (!activeOffers.isEmpty()) {
            Offer bestOffer = activeOffers.get(0);
            BigDecimal offerDiscount = originalPrice.multiply(bestOffer.getDiscountPercentage())
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
            discount = offerDiscount;
            discountDescription = "Offer: " + bestOffer.getTitle();
        }

        // Apply coupon (if provided and valid)
        if (couponCode != null && !couponCode.isBlank()) {
            Coupon coupon = couponRepository.findByCode(couponCode)
                .orElseThrow(() -> new NotFoundException("Coupon not found"));

            validateCoupon(coupon, originalPrice);

            BigDecimal couponDiscount;
            if ("PERCENTAGE".equals(coupon.getDiscountType())) {
                couponDiscount = originalPrice.multiply(coupon.getDiscountValue())
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
            } else {
                couponDiscount = coupon.getDiscountValue();
            }

            if (couponDiscount.compareTo(discount) > 0) {
                discount = couponDiscount;
                discountDescription = "Coupon: " + coupon.getCode();
            }
        }

        BigDecimal finalPrice = originalPrice.subtract(discount).max(BigDecimal.ZERO);
        return new PricingResult(originalPrice, discount, finalPrice, discountDescription);
    }

    private void validateCoupon(Coupon coupon, BigDecimal orderAmount) {
        if (!Boolean.TRUE.equals(coupon.getIsActive())) {
            throw new BadRequestException("Coupon is not active");
        }
        if (coupon.getExpiresAt() != null && coupon.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Coupon has expired");
        }
        if (coupon.getMaxUses() > 0 && coupon.getUsedCount() >= coupon.getMaxUses()) {
            throw new BadRequestException("Coupon usage limit reached");
        }
        if (orderAmount.compareTo(coupon.getMinOrderAmount()) < 0) {
            throw new BadRequestException("Order amount below minimum for this coupon");
        }
    }

    public record PricingResult(BigDecimal originalPrice, BigDecimal discount, BigDecimal finalPrice, String discountDescription) {}
}
