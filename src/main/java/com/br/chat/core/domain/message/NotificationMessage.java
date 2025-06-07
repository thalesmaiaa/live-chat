package com.br.chat.core.domain.message;

import com.br.chat.core.domain.user.User;
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
    private User senderUser;
    private NotificationType notificationType;
    private String destination;
}