# Deployment Guide

## Render Deployment (Zero Configuration)

The backend is configured for Render's `java` runtime via `render.yaml`. No changes are needed.

### Environment Variables

Render automatically sets:
- `DATABASE_URL` — PostgreSQL connection string
- `DATABASE_USERNAME` — Database username
- `DATABASE_PASSWORD` — Database password
- `JWT_SECRET` — Auto-generated secret
- `CORS_ALLOWED_ORIGINS` — Set to your frontend URL
- `PORT` — Auto-assigned by Render

### Build & Start

- **Build**: `mvn package -DskipTests`
- **Start**: `java -jar target/elearning-backend-1.0.0.jar`

### Database Migrations

Flyway runs automatically on startup with `baseline-on-migrate=true`. The old production schema is baselined at V1, and V2+ migrations upgrade it without data loss.

## Local Development

### Prerequisites

- Java 21+
- Maven 3.9+
- PostgreSQL 16 (or Docker)

### Setup

1. Start PostgreSQL:
   ```bash
   docker compose up -d postgres
   ```

2. Create `.env` file (see `.env.example`)

3. Run the application:
   ```bash
   mvn spring-boot:run
   ```

### Database

The database is created automatically by Flyway migrations. Seed data is inserted via `R__seed.sql`.

## Docker

```bash
docker compose up --build
```

This starts both PostgreSQL and the backend.

## Health Check

```
GET /api/v1/health
```

Returns `{"status": "UP"}` when healthy.
