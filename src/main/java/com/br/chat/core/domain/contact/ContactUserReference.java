package com.br.chat.core.domain.contact;

import com.br.chat.adapter.in.dto.responses.ContactResponse;
import com.br.chat.core.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class ContactUserReference {
    private Long id;
    private User user;
    private Boolean hasActiveChat;
    private UUID chatId;

    public ContactUserReference(Long id, User user) {
        this.id = id;
        this.user = user;
    }

    public ContactResponse toResponse() {
        return new ContactResponse(this.id, User.toUserResponse(this.user), this.hasActiveChat, this.chatId);
    }
}