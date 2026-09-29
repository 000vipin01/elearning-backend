-- V2: Evolve existing tables with new columns (idempotent, portable SQL).

-- users: email verification, avatar, bio, password reset
ALTER TABLE users ADD COLUMN IF NOT EXISTS email_verified BOOLEAN DEFAULT FALSE;
ALTER TABLE users ADD COLUMN IF NOT EXISTS avatar_url VARCHAR(500);
ALTER TABLE users ADD COLUMN IF NOT EXISTS bio TEXT;
ALTER TABLE users ADD COLUMN IF NOT EXISTS password_reset_token VARCHAR(255);
ALTER TABLE users ADD COLUMN IF NOT EXISTS password_reset_expires_at TIMESTAMP;

-- courses: pricing, status, thumbnail, level, timestamps
ALTER TABLE courses ADD COLUMN IF NOT EXISTS price DECIMAL(10,2) DEFAULT 0;
ALTER TABLE courses ADD COLUMN IF NOT EXISTS status VARCHAR(20) DEFAULT 'DRAFT';
ALTER TABLE courses ADD COLUMN IF NOT EXISTS thumbnail_url VARCHAR(500);
ALTER TABLE courses ADD COLUMN IF NOT EXISTS level VARCHAR(20) DEFAULT 'BEGINNER';
ALTER TABLE courses ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP;

-- lessons: media asset fields, free preview flag
ALTER TABLE lessons ADD COLUMN IF NOT EXISTS media_type VARCHAR(20);
ALTER TABLE lessons ADD COLUMN IF NOT EXISTS media_path VARCHAR(500);
ALTER TABLE lessons ADD COLUMN IF NOT EXISTS media_duration INT;
ALTER TABLE lessons ADD COLUMN IF NOT EXISTS media_size BIGINT;
ALTER TABLE lessons ADD COLUMN IF NOT EXISTS media_mime VARCHAR(100);
ALTER TABLE lessons ADD COLUMN IF NOT EXISTS is_free_preview BOOLEAN DEFAULT FALSE;
ALTER TABLE lessons ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP;

-- enrollments: link to order, price paid
ALTER TABLE enrollments ADD COLUMN IF NOT EXISTS order_id BIGINT;
ALTER TABLE enrollments ADD COLUMN IF NOT EXISTS price_paid DECIMAL(10,2);

-- quizzes: pass score, max attempts, shuffle flag
ALTER TABLE quizzes ADD COLUMN IF NOT EXISTS pass_score INT DEFAULT 60;
ALTER TABLE quizzes ADD COLUMN IF NOT EXISTS max_attempts INT DEFAULT 0;
ALTER TABLE quizzes ADD COLUMN IF NOT EXISTS shuffle_questions BOOLEAN DEFAULT FALSE;
