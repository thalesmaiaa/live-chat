package com.br.chat.core.port.in.auth;

import com.br.chat.adapter.in.dto.requests.LoginRequest;
import com.br.chat.adapter.in.dto.responses.LoginResponse;

public interface LoginPortIn {

    LoginResponse execute(LoginRequest request);
}
