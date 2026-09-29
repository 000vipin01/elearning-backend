# Manual Walkthrough Log

## Seeded Accounts (Development Only)

| Role | Email | Password |
|------|-------|----------|
| Admin | `admin@example.com` | `admin123` |
| Instructor | `instructor1@example.com` | `instructor123` |
| Student | `student1@example.com` | `student123` |

Additional seeded users:
- Instructors: `instructor2@example.com`, `instructor3@example.com` (password: `instructor123`)
- Students: `student2@example.com` through `student10@example.com` (password: `student123`)

## Walkthrough

### 1. Student Walkthrough

1. **Login**: Navigate to `/login`, enter `student1@example.com` / `student123`
2. **Dashboard**: View continue learning strip, stats, recommended courses
3. **Browse Courses**: Navigate to Courses, search/filter courses
4. **View Course**: Click a course to see details, lessons, pricing
5. **Enroll**: Click "Enroll Now" (free) or "Buy Now" (paid)
6. **Watch Lesson**: Click a lesson to open the video player
7. **Take Quiz**: Navigate to a quiz, answer questions, submit
8. **View Notifications**: Check the notification bell
9. **Update Progress**: Progress is tracked automatically from watched lessons

### 2. Instructor Walkthrough

1. **Login**: Navigate to `/login`, enter `instructor1@example.com` / `instructor123`
2. **Dashboard**: View stats, course pipeline
3. **Create Course**: Click "New Course", fill in details
4. **Add Lessons**: Add lessons to a course with media
5. **Create Quiz**: Add a quiz with questions
6. **Publish**: Publish the course when ready
7. **View Students**: See enrolled students in the roster
8. **View Earnings**: Check revenue and payout summary
9. **Create Offer**: Create a course-level offer (requires admin approval)

### 3. Admin Walkthrough

1. **Login**: Navigate to `/login`, enter `admin@example.com` / `admin123`
2. **Dashboard**: View platform analytics, revenue summary
3. **Manage Users**: View all users, change roles, delete users
4. **Approve Courses**: Review pending courses, approve or reject
5. **View Payments**: Check payments and refunds ledger
6. **Manage Coupons**: Create and manage coupons
7. **Manage Ads**: Create and manage advertisements
8. **Send Announcements**: Broadcast to all users or specific roles
9. **View Audit Log**: Review admin action history

## Verification Checklist

- [ ] Student can browse and enroll in courses
- [ ] Student can watch lessons with video player
- [ ] Student can take quizzes and view results
- [ ] Student can view notifications
- [ ] Instructor can create and manage courses
- [ ] Instructor can add lessons and quizzes
- [ ] Instructor can view students and earnings
- [ ] Admin can manage users and roles
- [ ] Admin can approve/reject courses
- [ ] Admin can view payments and issue refunds
- [ ] Admin can manage coupons, ads, and announcements
- [ ] Each role cannot access the others' URLs (redirects)
