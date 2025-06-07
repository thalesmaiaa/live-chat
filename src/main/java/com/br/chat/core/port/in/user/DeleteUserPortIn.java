package com.br.chat.core.port.in.user;

import java.util.UUID;

public interface DeleteUserPortIn {

    void execute(UUID id);
}
