package com.elearning.notifications.entity;

import com.elearning.users.entity.User;
import jakarta.persistence.*;

@Entity
@Table(name = "notification_preferences")
public class NotificationPreference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "email_on_enrollment")
    private Boolean emailOnEnrollment = true;

    @Column(name = "email_on_payment")
    private Boolean emailOnPayment = true;

    @Column(name = "email_on_course_update")
    private Boolean emailOnCourseUpdate = true;

    @Column(name = "email_on_announcement")
    private Boolean emailOnAnnouncement = true;

    @Column(name = "push_enabled")
    private Boolean pushEnabled = true;

    public NotificationPreference() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public Boolean getEmailOnEnrollment() { return emailOnEnrollment; }
    public void setEmailOnEnrollment(Boolean emailOnEnrollment) { this.emailOnEnrollment = emailOnEnrollment; }
    public Boolean getEmailOnPayment() { return emailOnPayment; }
    public void setEmailOnPayment(Boolean emailOnPayment) { this.emailOnPayment = emailOnPayment; }
    public Boolean getEmailOnCourseUpdate() { return emailOnCourseUpdate; }
    public void setEmailOnCourseUpdate(Boolean emailOnCourseUpdate) { this.emailOnCourseUpdate = emailOnCourseUpdate; }
    public Boolean getEmailOnAnnouncement() { return emailOnAnnouncement; }
    public void setEmailOnAnnouncement(Boolean emailOnAnnouncement) { this.emailOnAnnouncement = emailOnAnnouncement; }
    public Boolean getPushEnabled() { return pushEnabled; }
    public void setPushEnabled(Boolean pushEnabled) { this.pushEnabled = pushEnabled; }
}
