package com.br.chat.adapter.in.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class CreatePrivateChatRequest {

    private UUID senderId;
    private UUID receiverId;
    private String message;
}
