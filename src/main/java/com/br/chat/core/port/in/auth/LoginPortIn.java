package com.br.chat.core.port.in.auth;

import com.br.chat.adapter.in.dto.requests.LoginRequest;
import com.br.chat.adapter.in.dto.responses.AuthResponse;

public interface LoginPortIn {

    AuthResponse execute(LoginRequest request);
}
