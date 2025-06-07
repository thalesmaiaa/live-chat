package com.br.chat.core.usecase.user;

import com.br.chat.core.port.in.user.DeleteUserPortIn;
import com.br.chat.core.port.out.UserRepositoryPortOut;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DeleteUserUseCase implements DeleteUserPortIn {

    private final UserRepositoryPortOut userRepositoryPortOut;

    @Override
    public void execute(UUID id) {
        userRepositoryPortOut.deleteById(id);
    }
}
