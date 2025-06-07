package com.br.chat.core.domain.message;

import com.br.chat.adapter.in.dto.responses.MessageResponse;
import com.br.chat.adapter.in.dto.responses.UserResponse;
import com.br.chat.core.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
@AllArgsConstructor
public class Message {

    private User senderUser;
    private String content;
    private ZonedDateTime sentAt;

    public MessageResponse toMessageResponse() {
        var senderUser = new UserResponse(this.senderUser.getId(), this.senderUser.getEmail(), this.senderUser.getUsername(),
                this.senderUser.getCreatedAt(), this.senderUser.getUpdatedAt());
        return new MessageResponse(content, sentAt, senderUser);
    }


}