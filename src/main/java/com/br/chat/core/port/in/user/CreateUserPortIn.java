package com.br.chat.core.port.in.user;


import com.br.chat.core.domain.user.User;

public interface CreateUserPortIn {

    void execute(User user);
}
