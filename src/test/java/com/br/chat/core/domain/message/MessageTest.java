package com.br.chat.core.domain.message;

import com.br.chat.core.domain.user.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.util.UUID;

class MessageTest {

    @Test
    void shouldConvertToMessageResponseWithCorrectFields() {
        var userId = UUID.randomUUID();
        var email = "test@example.com";
        var username = "testuser";
        var user = new User(userId, email, username);

        var content = "Hello, world!";
        var sentAt = ZonedDateTime.now();

        var message = new Message(user, content, sentAt);

        var response = message.toMessageResponse();

        Assertions.assertThat(response.content()).isEqualTo(content);
        Assertions.assertThat(response.sentAt()).isEqualTo(sentAt);
        Assertions.assertThat(response.senderUser().id()).isEqualTo(userId);
        Assertions.assertThat(response.senderUser().email()).isEqualTo(email);
        Assertions.assertThat(response.senderUser().username()).isEqualTo(username);
    }
}
