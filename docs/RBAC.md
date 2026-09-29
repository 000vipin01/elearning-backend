# RBAC — Role-Based Access Control

## Roles

| Role | Description |
|------|-------------|
| `STUDENT` | Can browse, enroll, learn, take quizzes, view own payments |
| `INSTRUCTOR` | Can CRUD own courses/lessons/quizzes, view own students and earnings |
| `ADMIN` | Full platform management: users, courses, payments, coupons, ads, announcements |

## Permissions Matrix

### Auth Endpoints

| Endpoint | Method | STUDENT | INSTRUCTOR | ADMIN | Anonymous |
|----------|--------|---------|------------|-------|-----------|
| `/api/v1/auth/login` | POST | ✅ | ✅ | ✅ | ✅ |
| `/api/v1/auth/signup` | POST | ✅ | ✅ | ✅ | ✅ |
| `/api/v1/auth/password-reset` | POST | ✅ | ✅ | ✅ | ✅ |

### Course Endpoints

| Endpoint | Method | STUDENT | INSTRUCTOR | ADMIN | Anonymous |
|----------|--------|---------|------------|-------|-----------|
| `/api/v1/courses` | GET | ✅ | ✅ | ✅ | ✅ |
| `/api/v1/courses/{id}` | GET | ✅ | ✅ | ✅ | ✅ |
| `/api/v1/courses` | POST | ❌ 403 | ✅ | ✅ | ❌ 401 |
| `/api/v1/courses/{id}` | PUT | ❌ 403 | ✅ (owner) | ✅ | ❌ 401 |
| `/api/v1/courses/{id}` | DELETE | ❌ 403 | ✅ (owner) | ✅ | ❌ 401 |
| `/api/v1/courses/{id}/publish` | POST | ❌ 403 | ✅ (owner) | ✅ | ❌ 401 |
| `/api/v1/courses/{id}/reject` | POST | ❌ 403 | ❌ 403 | ✅ | ❌ 401 |
| `/api/v1/courses/instructor/mine` | GET | ❌ 403 | ✅ | ✅ | ❌ 401 |

### Lesson Endpoints

| Endpoint | Method | STUDENT | INSTRUCTOR | ADMIN | Anonymous |
|----------|--------|---------|------------|-------|-----------|
| `/api/v1/courses/{id}/lessons` | GET | ✅ | ✅ | ✅ | ✅ |
| `/api/v1/courses/{id}/lessons` | POST | ❌ 403 | ✅ (owner) | ✅ | ❌ 401 |
| `/api/v1/courses/{id}/lessons/{lid}` | PUT | ❌ 403 | ✅ (owner) | ✅ | ❌ 401 |
| `/api/v1/courses/{id}/lessons/{lid}` | DELETE | ❌ 403 | ✅ (owner) | ✅ | ❌ 401 |

### Enrollment Endpoints

| Endpoint | Method | STUDENT | INSTRUCTOR | ADMIN | Anonymous |
|----------|--------|---------|------------|-------|-----------|
| `/api/v1/enrollments/{courseId}` | POST | ✅ | ❌ 403 | ❌ 403 | ❌ 401 |
| `/api/v1/enrollments/my` | GET | ✅ | ❌ 403 | ❌ 403 | ❌ 401 |
| `/api/v1/enrollments/{courseId}` | GET | ✅ | ❌ 403 | ❌ 403 | ❌ 401 |
| `/api/v1/enrollments/{courseId}/progress` | PUT | ✅ | ❌ 403 | ❌ 403 | ❌ 401 |
| `/api/v1/enrollments/course/{courseId}` | GET | ❌ 403 | ✅ (owner) | ✅ | ❌ 401 |

### Quiz Endpoints

| Endpoint | Method | STUDENT | INSTRUCTOR | ADMIN | Anonymous |
|----------|--------|---------|------------|-------|-----------|
| `/api/v1/courses/{id}/quizzes` | GET | ✅ | ✅ | ✅ | ✅ |
| `/api/v1/courses/{id}/quizzes` | POST | ❌ 403 | ✅ (owner) | ✅ | ❌ 401 |
| `/api/v1/courses/{id}/quizzes/{qid}/submit` | POST | ✅ | ❌ 403 | ❌ 403 | ❌ 401 |
| `/api/v1/courses/{id}/quizzes/{qid}/attempts` | GET | ✅ | ❌ 403 | ❌ 403 | ❌ 401 |

### Payment Endpoints

| Endpoint | Method | STUDENT | INSTRUCTOR | ADMIN | Anonymous |
|----------|--------|---------|------------|-------|-----------|
| `/api/v1/payments/orders` | POST | ✅ | ❌ 403 | ❌ 403 | ❌ 401 |
| `/api/v1/payments/orders/{id}/pay` | POST | ✅ | ❌ 403 | ❌ 403 | ❌ 401 |
| `/api/v1/payments/orders/my` | GET | ✅ | ❌ 403 | ❌ 403 | ❌ 401 |
| `/api/v1/payments/refunds` | POST | ❌ 403 | ❌ 403 | ✅ | ❌ 401 |

### Notification Endpoints

| Endpoint | Method | STUDENT | INSTRUCTOR | ADMIN | Anonymous |
|----------|--------|---------|------------|-------|-----------|
| `/api/v1/notifications` | GET | ✅ | ✅ | ✅ | ❌ 401 |
| `/api/v1/notifications/unread-count` | GET | ✅ | ✅ | ✅ | ❌ 401 |
| `/api/v1/notifications/{id}/read` | PUT | ✅ | ✅ | ✅ | ❌ 401 |
| `/api/v1/notifications/read-all` | PUT | ✅ | ✅ | ✅ | ❌ 401 |

### Admin Endpoints

| Endpoint | Method | STUDENT | INSTRUCTOR | ADMIN | Anonymous |
|----------|--------|---------|------------|-------|-----------|
| `/api/v1/admin/stats` | GET | ❌ 403 | ❌ 403 | ✅ | ❌ 401 |
| `/api/v1/admin/users` | GET | ❌ 403 | ❌ 403 | ✅ | ❌ 401 |
| `/api/v1/admin/users/{id}/role` | PUT | ❌ 403 | ❌ 403 | ✅ | ❌ 401 |
| `/api/v1/admin/users/{id}` | DELETE | ❌ 403 | ❌ 403 | ✅ | ❌ 401 |
| `/api/v1/admin/courses/pending` | GET | ❌ 403 | ❌ 403 | ✅ | ❌ 401 |
| `/api/v1/admin/audit-log` | GET | ❌ 403 | ❌ 403 | ✅ | ❌ 401 |

### Media Endpoints

| Endpoint | Method | STUDENT | INSTRUCTOR | ADMIN | Anonymous |
|----------|--------|---------|------------|-------|-----------|
| `/api/v1/lessons/{id}/stream-token` | GET | ✅ | ✅ | ✅ | ❌ 401 |
| `/api/v1/media/stream` | GET | ✅ | ✅ | ✅ | ❌ 401 |

## Security Model

- **JWT**: Stateless, 24h expiration, role read from DB (not token claim)
- **Method Security**: `@PreAuthorize` on all protected endpoints
- **Ownership**: Instructors can only modify their own courses/lessons/quizzes
- **Error Codes**: 401 (unauthenticated), 403 (forbidden), 404 (not found)
