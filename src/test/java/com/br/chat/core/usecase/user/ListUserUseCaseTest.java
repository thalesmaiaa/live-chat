package com.br.chat.core.usecase.user;

import com.br.chat.adapter.in.dto.responses.UserResponse;
import com.br.chat.core.domain.user.User;
import com.br.chat.core.exception.UserNotFoundException;
import com.br.chat.core.port.out.UserRepositoryPortOut;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class ListUserUseCaseTest {

    private UserRepositoryPortOut userRepositoryPortOut;
    private ListUserUseCase listUserUseCase;

    @BeforeEach
    void setUp() {
        userRepositoryPortOut = mock(UserRepositoryPortOut.class);
        listUserUseCase = new ListUserUseCase(userRepositoryPortOut);
    }

    @Test
    void shouldReturnAllUsers() {
        User user = new User(UUID.randomUUID(), "email", "username");
        when(userRepositoryPortOut.findAll()).thenReturn(List.of(user));

        List<UserResponse> result = listUserUseCase.execute();
        assertThat(result).hasSize(1);
    }

    @Test
    void shouldReturnUserById() {
        UUID id = UUID.randomUUID();
        User user = new User(id, "email", "username");
        when(userRepositoryPortOut.findById(id)).thenReturn(Optional.of(user));

        UserResponse response = listUserUseCase.execute(id);
        assertThat(response.id()).isEqualTo(id);
    }

    @Test
    void shouldThrowWhenUserNotFound() {
        UUID id = UUID.randomUUID();
        when(userRepositoryPortOut.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> listUserUseCase.execute(id))
            .isInstanceOf(UserNotFoundException.class);
    }
}
