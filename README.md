# E-Learning Management System — Backend

Spring Boot 3.4 + PostgreSQL 16 backend for a role-based e-learning platform.

## Quick Start

```bash
# Start PostgreSQL (Docker)
docker compose up -d postgres

# Run the application
mvn spring-boot:run

# Or build and run
mvn clean package -DskipTests
java -jar target/elearning-backend-1.0.0.jar
```

## Configuration

All configuration is via environment variables. See `.env.example` for the full list.

| Variable | Default | Description |
|----------|---------|-------------|
| `DB_HOST` | `localhost` | Database host |
| `DB_PORT` | `5432` | Database port |
| `DB_NAME` | `elearning` | Database name |
| `DB_USERNAME` | `elearning` | Database username |
| `DB_PASSWORD` | `elearning123` | Database password |
| `JWT_SECRET` | (dev fallback) | JWT signing secret |
| `CORS_ALLOWED_ORIGINS` | `http://localhost:5173` | Allowed CORS origins |
| `PORT` | `8080` | Server port |
| `DATABASE_URL` | — | Render database URL (alternative to DB_* vars) |
| `ADMIN_EMAIL` | `admin@example.com` | Seeded admin email |
| `ADMIN_PASSWORD` | `admin123` | Seeded admin password |
| `PAYMENT_PROVIDER` | `mock` | Payment provider (`mock` or `razorpay`) |
| `RAZORPAY_KEY_ID` | — | Razorpay key ID (enables Razorpay) |
| `RAZORPAY_KEY_SECRET` | — | Razorpay key secret |

## API

- **Health**: `GET /api/v1/health`
- **Auth**: `POST /api/v1/auth/login`, `POST /api/v1/auth/signup`
- **Courses**: `GET /api/v1/courses`, `POST /api/v1/courses`
- **Enrollments**: `POST /api/v1/enrollments/{courseId}`
- **Payments**: `POST /api/v1/payments/orders`, `POST /api/v1/payments/orders/{id}/pay`
- **Media**: `GET /api/v1/lessons/{id}/stream-token`, `GET /api/v1/media/stream`
- **Notifications**: `GET /api/v1/notifications`, `PUT /api/v1/notifications/{id}/read`
- **Admin**: `GET /api/v1/admin/stats`, `GET /api/v1/admin/users`

## Database Migrations

Flyway migrations run automatically on startup:
- `V1__baseline_old_schema.sql` — Old production schema (baseline)
- `V2__evolve_core.sql` — New columns on existing tables
- `V3__new_tables.sql` — New tables (payments, coupons, offers, ads, notifications, etc.)
- `R__seed.sql` — Idempotent seed data

## Testing

```bash
mvn verify
```

Tests use H2 in PostgreSQL mode (no Docker required). JaCoCo coverage report is generated at `target/site/jacoco/`.

## Deployment

See [DEPLOYMENT.md](./DEPLOYMENT.md) for Render deployment instructions.
