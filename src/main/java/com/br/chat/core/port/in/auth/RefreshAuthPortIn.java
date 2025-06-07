package com.br.chat.core.port.in.auth;

import com.br.chat.adapter.in.dto.responses.AuthResponse;

import java.util.UUID;

public interface RefreshAuthPortIn {

    AuthResponse execute(UUID userId);
}
