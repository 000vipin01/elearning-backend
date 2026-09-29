package com.elearning.notifications.repository;

import com.elearning.notifications.entity.NotificationPreference;
import com.elearning.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NotificationPreferenceRepository extends JpaRepository<NotificationPreference, Long> {
    Optional<NotificationPreference> findByUser(User user);
}
