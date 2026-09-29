package com.elearning.payments;

import com.elearning.common.error.BadRequestException;
import com.elearning.common.error.NotFoundException;
import com.elearning.coupons.entity.Coupon;
import com.elearning.coupons.repository.CouponRepository;
import com.elearning.courses.entity.Course;
import com.elearning.offers.entity.Offer;
import com.elearning.offers.repository.OfferRepository;
import com.elearning.payments.service.PricingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PricingServiceTest {

    @Mock private CouponRepository couponRepository;
    @Mock private OfferRepository offerRepository;

    @InjectMocks
    private PricingService pricingService;

    @Test
    void calculatePrice_noCouponNoOffer() {
        Course course = new Course();
        course.setId(1L);
        course.setPrice(BigDecimal.valueOf(1000));

        when(offerRepository.findByCourseId(1L)).thenReturn(List.of());

        PricingService.PricingResult result = pricingService.calculatePrice(course, null);

        assertEquals(BigDecimal.valueOf(1000), result.originalPrice());
        assertEquals(BigDecimal.ZERO, result.discount());
        assertEquals(0, result.finalPrice().compareTo(BigDecimal.valueOf(1000)));
    }

    @Test
    void calculatePrice_withPercentageCoupon() {
        Course course = new Course();
        course.setId(1L);
        course.setPrice(BigDecimal.valueOf(1000));

        Coupon coupon = new Coupon();
        coupon.setCode("SAVE20");
        coupon.setDiscountType("PERCENTAGE");
        coupon.setDiscountValue(BigDecimal.valueOf(20));
        coupon.setIsActive(true);
        coupon.setMaxUses(100);
        coupon.setUsedCount(0);
        coupon.setMinOrderAmount(BigDecimal.ZERO);

        when(offerRepository.findByCourseId(1L)).thenReturn(List.of());
        when(couponRepository.findByCode("SAVE20")).thenReturn(Optional.of(coupon));

        PricingService.PricingResult result = pricingService.calculatePrice(course, "SAVE20");

        assertEquals(BigDecimal.valueOf(1000), result.originalPrice());
        assertEquals(0, result.discount().compareTo(BigDecimal.valueOf(200)));
        assertEquals(0, result.finalPrice().compareTo(BigDecimal.valueOf(800)));
    }

    @Test
    void calculatePrice_withFixedCoupon() {
        Course course = new Course();
        course.setId(1L);
        course.setPrice(BigDecimal.valueOf(1000));

        Coupon coupon = new Coupon();
        coupon.setCode("FLAT100");
        coupon.setDiscountType("FIXED");
        coupon.setDiscountValue(BigDecimal.valueOf(100));
        coupon.setIsActive(true);
        coupon.setMaxUses(100);
        coupon.setUsedCount(0);
        coupon.setMinOrderAmount(BigDecimal.ZERO);

        when(offerRepository.findByCourseId(1L)).thenReturn(List.of());
        when(couponRepository.findByCode("FLAT100")).thenReturn(Optional.of(coupon));

        PricingService.PricingResult result = pricingService.calculatePrice(course, "FLAT100");

        assertEquals(BigDecimal.valueOf(1000), result.originalPrice());
        assertEquals(0, result.discount().compareTo(BigDecimal.valueOf(100)));
        assertEquals(0, result.finalPrice().compareTo(BigDecimal.valueOf(900)));
    }

    @Test
    void calculatePrice_withOffer() {
        Course course = new Course();
        course.setId(1L);
        course.setPrice(BigDecimal.valueOf(1000));

        Offer offer = new Offer();
        offer.setTitle("Summer Sale");
        offer.setDiscountPercentage(BigDecimal.valueOf(25));
        offer.setStatus("APPROVED");
        offer.setStartsAt(LocalDateTime.now().minusDays(1));
        offer.setEndsAt(LocalDateTime.now().plusDays(1));

        when(offerRepository.findByCourseId(1L)).thenReturn(List.of(offer));

        PricingService.PricingResult result = pricingService.calculatePrice(course, null);

        assertEquals(BigDecimal.valueOf(1000), result.originalPrice());
        assertEquals(0, result.discount().compareTo(BigDecimal.valueOf(250)));
        assertEquals(0, result.finalPrice().compareTo(BigDecimal.valueOf(750)));
    }

    @Test
    void calculatePrice_couponNotFound() {
        Course course = new Course();
        course.setId(1L);
        course.setPrice(BigDecimal.valueOf(1000));

        when(offerRepository.findByCourseId(1L)).thenReturn(List.of());
        when(couponRepository.findByCode("INVALID")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> pricingService.calculatePrice(course, "INVALID"));
    }

    @Test
    void calculatePrice_expiredCoupon() {
        Course course = new Course();
        course.setId(1L);
        course.setPrice(BigDecimal.valueOf(1000));

        Coupon coupon = new Coupon();
        coupon.setCode("EXPIRED");
        coupon.setDiscountType("PERCENTAGE");
        coupon.setDiscountValue(BigDecimal.valueOf(20));
        coupon.setIsActive(true);
        coupon.setExpiresAt(LocalDateTime.now().minusDays(1));

        when(offerRepository.findByCourseId(1L)).thenReturn(List.of());
        when(couponRepository.findByCode("EXPIRED")).thenReturn(Optional.of(coupon));

        assertThrows(BadRequestException.class, () -> pricingService.calculatePrice(course, "EXPIRED"));
    }

    @Test
    void calculatePrice_inactiveCoupon() {
        Course course = new Course();
        course.setId(1L);
        course.setPrice(BigDecimal.valueOf(1000));

        Coupon coupon = new Coupon();
        coupon.setCode("INACTIVE");
        coupon.setDiscountType("PERCENTAGE");
        coupon.setDiscountValue(BigDecimal.valueOf(20));
        coupon.setIsActive(false);

        when(offerRepository.findByCourseId(1L)).thenReturn(List.of());
        when(couponRepository.findByCode("INACTIVE")).thenReturn(Optional.of(coupon));

        assertThrows(BadRequestException.class, () -> pricingService.calculatePrice(course, "INACTIVE"));
    }

    @Test
    void calculatePrice_usageLimitReached() {
        Course course = new Course();
        course.setId(1L);
        course.setPrice(BigDecimal.valueOf(1000));

        Coupon coupon = new Coupon();
        coupon.setCode("MAXED");
        coupon.setDiscountType("PERCENTAGE");
        coupon.setDiscountValue(BigDecimal.valueOf(20));
        coupon.setIsActive(true);
        coupon.setMaxUses(10);
        coupon.setUsedCount(10);

        when(offerRepository.findByCourseId(1L)).thenReturn(List.of());
        when(couponRepository.findByCode("MAXED")).thenReturn(Optional.of(coupon));

        assertThrows(BadRequestException.class, () -> pricingService.calculatePrice(course, "MAXED"));
    }

    @Test
    void calculatePrice_freeCourse() {
        Course course = new Course();
        course.setId(1L);
        course.setPrice(BigDecimal.ZERO);

        when(offerRepository.findByCourseId(1L)).thenReturn(List.of());

        PricingService.PricingResult result = pricingService.calculatePrice(course, null);

        assertEquals(BigDecimal.ZERO, result.originalPrice());
        assertEquals(BigDecimal.ZERO, result.discount());
        assertEquals(BigDecimal.ZERO, result.finalPrice());
    }
}
