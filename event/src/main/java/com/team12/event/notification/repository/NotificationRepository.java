package com.team12.event.notification.repository;

import com.team12.clients.notification.dto.NotificationType;
import com.team12.event.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {

    List<Notification> findAllByToUserId(UUID toUserId);

    void deleteBytoUserIdAndNotificationType(UUID toUserId, NotificationType notificationType);

}