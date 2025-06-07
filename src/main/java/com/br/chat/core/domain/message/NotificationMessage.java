package com.br.chat.core.domain.message;

import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.UUID;

@Getter
@Setter
public class NotificationMessage {

    private String message;
    private UUID destinationId;
    private ZonedDateTime sentAt;
    private SenderUser senderUser;
    private NotificationType notificationType;
    private String destination;

    public record SenderUser(UUID id, String username, String email) {
    }
}