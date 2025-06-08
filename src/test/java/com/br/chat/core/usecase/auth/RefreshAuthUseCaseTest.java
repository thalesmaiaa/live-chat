package com.br.chat.core.usecase.auth;

import com.br.chat.core.domain.user.User;
import com.br.chat.core.exception.UserNotFoundException;
import com.br.chat.core.port.out.UserRepositoryPortOut;
import com.br.chat.core.service.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RefreshAuthUseCaseTest {

    @Mock
    private TokenService tokenService;
    @Mock
    private UserRepositoryPortOut userRepositoryPortOut;

    @InjectMocks
    private RefreshAuthUseCase refreshAuthUseCase;

    @Test
    void shouldRefreshTokens() {
        var userId = UUID.randomUUID();
        var user = new User(userId, "email", "username");
        when(userRepositoryPortOut.findById(userId)).thenReturn(Optional.of(user));
        when(tokenService.generateAccessToken(anyString(), anyString())).thenReturn("access");
        when(tokenService.generateRefreshToken(anyString())).thenReturn("refresh");

        var response = refreshAuthUseCase.execute(userId);
        assertThat(response.accessToken()).isEqualTo("access");
        assertThat(response.refreshToken()).isEqualTo("refresh");
    }

    @Test
    void shouldThrowWhenUserNotFound() {
        var userId = UUID.randomUUID();
        when(userRepositoryPortOut.findById(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> refreshAuthUseCase.execute(userId))
            .isInstanceOf(UserNotFoundException.class);
    }
}
