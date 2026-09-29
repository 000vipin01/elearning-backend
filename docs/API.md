# API Reference

Base URL: `/api/v1`

## Authentication

All protected endpoints require a JWT token in the `Authorization` header:
```
Authorization: Bearer <token>
```

## Error Response

All errors return a consistent JSON shape:

```json
{
  "status": 404,
  "error": "Not Found",
  "message": "Course not found",
  "path": "/api/v1/courses/999",
  "timestamp": "2026-09-29T12:00:00"
}
```

## Pagination

List endpoints accept `page` (0-based) and `size` parameters and return:

```json
{
  "content": [...],
  "page": 0,
  "size": 10,
  "totalElements": 100,
  "totalPages": 10,
  "first": true,
  "last": false
}
```

## Endpoints

### Auth

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/auth/login` | Login with email/password |
| POST | `/auth/signup` | Create student account |
| POST | `/auth/password-reset` | Request password reset |
| POST | `/auth/password-reset/confirm` | Confirm password reset |

### Courses

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/courses` | List courses (public, published only) |
| GET | `/courses/{id}` | Get course detail |
| POST | `/courses` | Create course (instructor) |
| PUT | `/courses/{id}` | Update course (owner) |
| DELETE | `/courses/{id}` | Delete course (owner) |
| POST | `/courses/{id}/publish` | Publish course (owner) |
| POST | `/courses/{id}/reject` | Reject course (admin) |
| GET | `/courses/instructor/mine` | List own courses (instructor) |

### Lessons

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/courses/{id}/lessons` | List course lessons |
| POST | `/courses/{id}/lessons` | Create lesson (owner) |
| PUT | `/courses/{id}/lessons/{lid}` | Update lesson (owner) |
| DELETE | `/courses/{id}/lessons/{lid}` | Delete lesson (owner) |

### Enrollments

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/enrollments/{courseId}` | Enroll in course (student) |
| GET | `/enrollments/my` | List my enrollments (student) |
| GET | `/enrollments/{courseId}` | Get enrollment details (student) |
| PUT | `/enrollments/{courseId}/progress` | Update progress (student) |
| GET | `/enrollments/course/{courseId}` | List course enrollments (owner) |

### Quizzes

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/courses/{id}/quizzes` | List course quizzes |
| POST | `/courses/{id}/quizzes` | Create quiz (owner) |
| PUT | `/courses/{id}/quizzes/{qid}` | Update quiz (owner) |
| DELETE | `/courses/{id}/quizzes/{qid}` | Delete quiz (owner) |
| POST | `/courses/{id}/quizzes/{qid}/questions` | Add question (owner) |
| POST | `/courses/{id}/quizzes/{qid}/submit` | Submit quiz (student) |
| GET | `/courses/{id}/quizzes/{qid}/attempts` | List my attempts (student) |
| GET | `/courses/{id}/quizzes/{qid}/best-score` | Get best score (student) |

### Payments

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/payments/orders` | Create order (student) |
| POST | `/payments/orders/{id}/pay` | Process payment (student) |
| POST | `/payments/verify` | Verify payment (student) |
| POST | `/payments/refunds` | Refund payment (admin) |
| GET | `/payments/orders/my` | List my orders (student) |
| GET | `/payments/orders/{id}` | List order payments (student) |

### Coupons

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/coupons` | List all coupons |
| POST | `/coupons` | Create coupon (admin) |
| PUT | `/coupons/{id}` | Update coupon (admin) |
| DELETE | `/coupons/{id}` | Delete coupon (admin) |

### Offers

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/offers` | List offers |
| GET | `/offers/course/{id}` | List course offers |
| POST | `/offers` | Create offer (instructor) |
| POST | `/offers/{id}/approve` | Approve offer (admin) |
| POST | `/offers/{id}/reject` | Reject offer (admin) |

### Ads

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/ads/active` | Get active ads |
| GET | `/ads` | List all ads (admin) |
| POST | `/ads` | Create ad (admin) |
| PUT | `/ads/{id}` | Update ad (admin) |
| DELETE | `/ads/{id}` | Delete ad (admin) |
| POST | `/ads/{id}/impression` | Track impression |
| POST | `/ads/{id}/click` | Track click |

### Notifications

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/notifications` | List my notifications |
| GET | `/notifications/unread-count` | Get unread count |
| PUT | `/notifications/{id}/read` | Mark as read |
| PUT | `/notifications/read-all` | Mark all as read |

### Media

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/lessons/{id}/stream-token` | Get stream token |
| GET | `/media/stream` | Stream media (Range support) |

### Admin

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/admin/stats` | Platform statistics |
| GET | `/admin/users` | List all users |
| PUT | `/admin/users/{id}/role` | Update user role |
| DELETE | `/admin/users/{id}` | Delete user |
| GET | `/admin/courses/pending` | List pending courses |
| POST | `/admin/courses/{id}/approve` | Approve course |
| POST | `/admin/courses/{id}/reject` | Reject course |
| GET | `/admin/audit-log` | View audit log |

### Health

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/health` | Health check |
