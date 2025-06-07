package com.br.chat.core.domain.message;

import lombok.Getter;

@Getter
public enum NotificationType {
    CONTACT_REQUEST("You have a new contact request from %s"),
    NEW_MESSAGE("You have received a message from %s on chat %s");

    private final String message;

    NotificationType(String message) {
        this.message = message;
    }
}
