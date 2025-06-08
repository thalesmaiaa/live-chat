package com.br.chat.core.usecase.user;

import com.br.chat.core.port.out.UserRepositoryPortOut;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DeleteUserUseCaseTest {

    @Mock
    private UserRepositoryPortOut userRepositoryPortOut;

    @InjectMocks
    private DeleteUserUseCase deleteUserUseCase;

    @Test
    void shouldDeleteUserById() {
        var userId = UUID.randomUUID();
        deleteUserUseCase.execute(userId);
        verify(userRepositoryPortOut).deleteById(userId);
    }
}
