package com.br.chat.core.usecase.user;

import com.br.chat.core.domain.user.User;
import com.br.chat.core.exception.UserNotFoundException;
import com.br.chat.core.port.out.UserRepositoryPortOut;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListUserUseCaseTest {

    @Mock
    private UserRepositoryPortOut userRepositoryPortOut;

    @InjectMocks
    private ListUserUseCase listUserUseCase;

    @Test
    void shouldReturnAllUsers() {
        var user = new User(UUID.randomUUID(), "email", "username");
        when(userRepositoryPortOut.findAll()).thenReturn(List.of(user));

        var result = listUserUseCase.execute();
        assertThat(result).hasSize(1);
    }

    @Test
    void shouldReturnUserById() {
        var id = UUID.randomUUID();
        var user = new User(id, "email", "username");
        when(userRepositoryPortOut.findById(id)).thenReturn(Optional.of(user));

        var response = listUserUseCase.execute(id);
        assertThat(response.id()).isEqualTo(id);
    }

    @Test
    void shouldThrowWhenUserNotFound() {
        var id = UUID.randomUUID();
        when(userRepositoryPortOut.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> listUserUseCase.execute(id))
            .isInstanceOf(UserNotFoundException.class);
    }
}
