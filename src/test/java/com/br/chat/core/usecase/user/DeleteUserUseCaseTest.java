package com.br.chat.core.usecase.user;

import com.br.chat.core.port.out.UserRepositoryPortOut;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.mockito.Mockito.*;

class DeleteUserUseCaseTest {

    private UserRepositoryPortOut userRepositoryPortOut;
    private DeleteUserUseCase deleteUserUseCase;

    @BeforeEach
    void setUp() {
        userRepositoryPortOut = mock(UserRepositoryPortOut.class);
        deleteUserUseCase = new DeleteUserUseCase(userRepositoryPortOut);
    }

    @Test
    void shouldDeleteUserById() {
        UUID userId = UUID.randomUUID();
        deleteUserUseCase.execute(userId);
        verify(userRepositoryPortOut).deleteById(userId);
    }
}
