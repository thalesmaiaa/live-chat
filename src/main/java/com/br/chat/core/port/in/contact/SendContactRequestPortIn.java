package com.br.chat.core.port.in.contact;

import java.util.UUID;

public interface SendContactRequestPortIn {

    void execute(UUID userId, String email);

}
