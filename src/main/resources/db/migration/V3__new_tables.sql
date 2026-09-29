-- V3: New tables for payments, coupons, offers, ads, notifications, quizzes, audit, etc.
-- Portable SQL: runs on PostgreSQL 16 and H2 in PostgreSQL mode.

-- ============ ORDERS & PAYMENTS ============

CREATE TABLE IF NOT EXISTS orders (
    id BIGSERIAL PRIMARY KEY,
    student_id BIGINT NOT NULL REFERENCES users(id),
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    total_amount DECIMAL(10,2) NOT NULL DEFAULT 0,
    discount_amount DECIMAL(10,2) DEFAULT 0,
    final_amount DECIMAL(10,2) NOT NULL DEFAULT 0,
    coupon_id BIGINT,
    idempotency_key VARCHAR(100) UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS order_items (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL REFERENCES orders(id),
    course_id BIGINT NOT NULL REFERENCES courses(id),
    original_price DECIMAL(10,2) NOT NULL,
    discount_amount DECIMAL(10,2) DEFAULT 0,
    final_price DECIMAL(10,2) NOT NULL
);

CREATE TABLE IF NOT EXISTS payments (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL REFERENCES orders(id),
    provider VARCHAR(20) NOT NULL DEFAULT 'MOCK',
    provider_payment_id VARCHAR(255),
    provider_order_id VARCHAR(255),
    amount DECIMAL(10,2) NOT NULL,
    currency VARCHAR(3) DEFAULT 'INR',
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    idempotency_key VARCHAR(100) UNIQUE,
    signature_verified BOOLEAN DEFAULT FALSE,
    failure_reason TEXT,
    paid_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS refunds (
    id BIGSERIAL PRIMARY KEY,
    payment_id BIGINT NOT NULL REFERENCES payments(id),
    order_id BIGINT NOT NULL REFERENCES orders(id),
    amount DECIMAL(10,2) NOT NULL,
    reason TEXT,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    processed_by BIGINT REFERENCES users(id),
    provider_refund_id VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    processed_at TIMESTAMP
);

-- ============ COUPONS & OFFERS ============

CREATE TABLE IF NOT EXISTS coupons (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE,
    description TEXT,
    discount_type VARCHAR(10) NOT NULL DEFAULT 'PERCENTAGE',
    discount_value DECIMAL(10,2) NOT NULL,
    max_uses INT DEFAULT 0,
    used_count INT DEFAULT 0,
    max_uses_per_user INT DEFAULT 1,
    min_order_amount DECIMAL(10,2) DEFAULT 0,
    starts_at TIMESTAMP,
    expires_at TIMESTAMP,
    is_active BOOLEAN DEFAULT TRUE,
    created_by BIGINT REFERENCES users(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS coupon_redemptions (
    id BIGSERIAL PRIMARY KEY,
    coupon_id BIGINT NOT NULL REFERENCES coupons(id),
    user_id BIGINT NOT NULL REFERENCES users(id),
    order_id BIGINT REFERENCES orders(id),
    redeemed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS offers (
    id BIGSERIAL PRIMARY KEY,
    course_id BIGINT NOT NULL REFERENCES courses(id),
    title VARCHAR(255) NOT NULL,
    description TEXT,
    discount_percentage DECIMAL(5,2) NOT NULL,
    starts_at TIMESTAMP NOT NULL,
    ends_at TIMESTAMP NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    created_by BIGINT NOT NULL REFERENCES users(id),
    reviewed_by BIGINT REFERENCES users(id),
    reviewed_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============ ADVERTISEMENTS ============

CREATE TABLE IF NOT EXISTS ads (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    image_url VARCHAR(500),
    target_url VARCHAR(500),
    placement_slot VARCHAR(50) NOT NULL,
    target_role VARCHAR(20),
    starts_at TIMESTAMP,
    ends_at TIMESTAMP,
    is_active BOOLEAN DEFAULT TRUE,
    impressions INT DEFAULT 0,
    clicks INT DEFAULT 0,
    created_by BIGINT REFERENCES users(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS ad_events (
    id BIGSERIAL PRIMARY KEY,
    ad_id BIGINT NOT NULL REFERENCES ads(id),
    user_id BIGINT REFERENCES users(id),
    event_type VARCHAR(10) NOT NULL,
    session_id VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============ NOTIFICATIONS ============

CREATE TABLE IF NOT EXISTS notifications (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id),
    type VARCHAR(30) NOT NULL,
    title VARCHAR(255) NOT NULL,
    body TEXT,
    link VARCHAR(500),
    is_read BOOLEAN DEFAULT FALSE,
    read_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS notification_preferences (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) UNIQUE,
    email_on_enrollment BOOLEAN DEFAULT TRUE,
    email_on_payment BOOLEAN DEFAULT TRUE,
    email_on_course_update BOOLEAN DEFAULT TRUE,
    email_on_announcement BOOLEAN DEFAULT TRUE,
    push_enabled BOOLEAN DEFAULT TRUE
);

-- ============ QUIZ ATTEMPTS & LESSON PROGRESS ============

CREATE TABLE IF NOT EXISTS quiz_attempts (
    id BIGSERIAL PRIMARY KEY,
    quiz_id BIGINT NOT NULL REFERENCES quizzes(id),
    student_id BIGINT NOT NULL REFERENCES users(id),
    score INT DEFAULT 0,
    total_questions INT DEFAULT 0,
    percentage DECIMAL(5,2) DEFAULT 0,
    passed BOOLEAN DEFAULT FALSE,
    answers TEXT,
    started_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    completed_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS lesson_progress (
    id BIGSERIAL PRIMARY KEY,
    enrollment_id BIGINT NOT NULL REFERENCES enrollments(id),
    lesson_id BIGINT NOT NULL REFERENCES lessons(id),
    watched_seconds INT DEFAULT 0,
    completed BOOLEAN DEFAULT FALSE,
    last_position_seconds INT DEFAULT 0,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(enrollment_id, lesson_id)
);

-- ============ AUDIT & ADMIN ============

CREATE TABLE IF NOT EXISTS audit_log (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id),
    action VARCHAR(50) NOT NULL,
    entity_type VARCHAR(50),
    entity_id BIGINT,
    details TEXT,
    ip_address VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS password_reset_tokens (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id),
    token VARCHAR(255) NOT NULL UNIQUE,
    expires_at TIMESTAMP NOT NULL,
    used BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS instructor_applications (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id),
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    reason TEXT,
    reviewed_by BIGINT REFERENCES users(id),
    reviewed_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS announcements (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    body TEXT NOT NULL,
    target_role VARCHAR(20),
    created_by BIGINT NOT NULL REFERENCES users(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
