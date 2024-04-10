package com.foodzy.notificationservice.service;

import com.foodzy.notificationservice.dto.EventDto;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaConsumerService {
    private final SimpMessagingTemplate messagingTemplate;

    @KafkaListener(topics = "notifications", groupId = "group1")
    public void receiveMessage(EventDto event) {
        System.out.println(event);
        String destination = "/topics/notifications/" + event.userId();
        String message = event.message();
        sendNotificationToWebsocket(destination, message);
    }

    @SendTo("/topics/notifications")
    public void sendNotificationToWebsocket(String destination, String message) {
        messagingTemplate.convertAndSend(destination, message);
    }
}