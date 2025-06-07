package com.br.chat.core.port.in.user;

import com.br.chat.adapter.in.dto.responses.UserResponse;

import java.util.List;
import java.util.UUID;

public interface ListUserPortIn {

    UserResponse execute(UUID id);

    List<UserResponse> execute();
}
