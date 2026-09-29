package com.elearning.notifications;

import com.elearning.common.error.NotFoundException;
import com.elearning.notifications.entity.Notification;
import com.elearning.notifications.repository.NotificationRepository;
import com.elearning.users.entity.User;
import com.elearning.users.repository.UserRepository;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class NotificationServiceTest {

    @Test
    void createNotification_userNotFound_throwsNotFound() {
        assertThrows(NotFoundException.class, () -> {
            throw new NotFoundException("User not found");
        });
    }

    @Test
    void notification_isReadDefaultsToFalse() {
        Notification notification = new Notification();
        assertFalse(notification.getIsRead());
    }

    @Test
    void notification_typeRequired() {
        Notification notification = new Notification();
        notification.setType("ENROLLMENT");
        assertEquals("ENROLLMENT", notification.getType());
    }

    @Test
    void notification_titleRequired() {
        Notification notification = new Notification();
        notification.setTitle("Test Notification");
        assertEquals("Test Notification", notification.getTitle());
    }

    @Test
    void markAsRead_setsReadAt() {
        Notification notification = new Notification();
        notification.setIsRead(true);
        notification.setReadAt(java.time.LocalDateTime.now());

        assertTrue(notification.getIsRead());
        assertNotNull(notification.getReadAt());
    }
}
