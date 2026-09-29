package com.elearning.notifications.service;

import com.elearning.common.error.NotFoundException;
import com.elearning.common.security.AuthenticatedUser;
import com.elearning.common.security.SecurityUtils;
import com.elearning.notifications.dto.NotificationResponse;
import com.elearning.notifications.entity.Notification;
import com.elearning.notifications.repository.NotificationRepository;
import com.elearning.users.entity.User;
import com.elearning.users.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public NotificationService(NotificationRepository notificationRepository, UserRepository userRepository) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Notification createNotification(Long userId, String type, String title, String body, String link) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException("User not found"));

        Notification notification = new Notification();
        notification.setUser(user);
        notification.setType(type);
        notification.setTitle(title);
        notification.setBody(body);
        notification.setLink(link);
        notification.setIsRead(false);

        return notificationRepository.save(notification);
    }

    public List<NotificationResponse> getMyNotifications(int page, int size) {
        AuthenticatedUser currentUser = SecurityUtils.getCurrentUser();
        User user = userRepository.findById(currentUser.id())
            .orElseThrow(() -> new NotFoundException("User not found"));

        Pageable pageable = PageRequest.of(page, size);
        Page<Notification> notifications = notificationRepository.findByUser(user, pageable);

        return notifications.getContent().stream().map(this::toResponse).toList();
    }

    public long getUnreadCount() {
        AuthenticatedUser currentUser = SecurityUtils.getCurrentUser();
        User user = userRepository.findById(currentUser.id())
            .orElseThrow(() -> new NotFoundException("User not found"));

        return notificationRepository.countByUserAndIsReadFalse(user);
    }

    @Transactional
    public NotificationResponse markAsRead(Long notificationId) {
        AuthenticatedUser currentUser = SecurityUtils.getCurrentUser();
        Notification notification = notificationRepository.findById(notificationId)
            .filter(n -> n.getUser().getId().equals(currentUser.id()))
            .orElseThrow(() -> new NotFoundException("Notification not found"));

        notification.setIsRead(true);
        notification.setReadAt(LocalDateTime.now());
        notificationRepository.save(notification);

        return toResponse(notification);
    }

    @Transactional
    public void markAllAsRead() {
        AuthenticatedUser currentUser = SecurityUtils.getCurrentUser();
        User user = userRepository.findById(currentUser.id())
            .orElseThrow(() -> new NotFoundException("User not found"));

        List<Notification> unread = notificationRepository.findByUser(user, Pageable.unpaged()).getContent()
            .stream().filter(n -> !n.getIsRead()).toList();

        for (Notification n : unread) {
            n.setIsRead(true);
            n.setReadAt(LocalDateTime.now());
        }
        notificationRepository.saveAll(unread);
    }

    private NotificationResponse toResponse(Notification n) {
        return new NotificationResponse(n.getId(), n.getType(), n.getTitle(), n.getBody(),
            n.getLink(), n.getIsRead(), n.getReadAt(), n.getCreatedAt());
    }
}
