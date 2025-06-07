package com.br.chat.core.domain.chat;

import com.br.chat.core.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class PrivateChat {
    private UUID id;
    private User user;
}
