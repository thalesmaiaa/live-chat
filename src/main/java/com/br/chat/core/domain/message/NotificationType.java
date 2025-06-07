package com.br.chat.core.domain.message;

import lombok.Getter;

@Getter
public enum NotificationType {
    CONTACT_REQUEST("You have a new contact request from %s", "/topics/notifications/%s"),
    UNREAD_MESSAGES("You have unread messages from %s", "/topics/notifications/%s"),
    NEW_MESSAGE("You have received a message from %s on chat %s", "/topics/notifications/%s");

    private final String message;
    private final String notificationTopic;


    NotificationType(String message, String notificationTopic) {
        this.message = message;
        this.notificationTopic = notificationTopic;
    }
}
