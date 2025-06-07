package com.br.chat.adapter.in.dto.responses;

import java.time.ZonedDateTime;

public record MessageResponse(String content, ZonedDateTime sentAt, UserResponse senderUser) {
}
