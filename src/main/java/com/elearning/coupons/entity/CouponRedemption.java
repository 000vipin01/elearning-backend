package com.elearning.coupons.entity;

import com.elearning.users.entity.User;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "coupon_redemptions")
public class CouponRedemption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id", nullable = false)
    private Coupon coupon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "redeemed_at")
    private LocalDateTime redeemedAt;

    @PrePersist
    protected void onCreate() {
        redeemedAt = LocalDateTime.now();
    }

    public CouponRedemption() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Coupon getCoupon() { return coupon; }
    public void setCoupon(Coupon coupon) { this.coupon = coupon; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
    public LocalDateTime getRedeemedAt() { return redeemedAt; }
    public void setRedeemedAt(LocalDateTime redeemedAt) { this.redeemedAt = redeemedAt; }
}
