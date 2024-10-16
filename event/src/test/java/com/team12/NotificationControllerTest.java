package com.team12;

import static org.mockito.Mockito.*;

import com.team12.clients.notification.dto.NotificationRequest;
import com.team12.event.notification.controller.NotificationController;
import com.team12.event.notification.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@SpringBootTest
class NotificationControllerTest {

    private NotificationService notificationService;
    private NotificationController notificationController;

    @BeforeEach
    void setUp() {
        notificationService = mock(NotificationService.class);
        notificationController = new NotificationController(notificationService);
    }

    @Test
    void sendNotification_successful() {
        NotificationRequest request = new NotificationRequest(UUID.randomUUID(),"Notification content","test.com");

        doNothing().when(notificationService).sendNotification(request);

        notificationController.sendNotification(request);

        verify(notificationService, times(1)).sendNotification(request);
    }
}
