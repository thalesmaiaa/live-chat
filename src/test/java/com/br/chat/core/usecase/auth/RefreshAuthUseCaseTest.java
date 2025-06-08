package com.br.chat.core.usecase.auth;

import com.br.chat.core.domain.user.User;
import com.br.chat.core.exception.UserNotFoundException;
import com.br.chat.core.port.out.UserRepositoryPortOut;
import com.br.chat.core.service.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class RefreshAuthUseCaseTest {

    private TokenService tokenService;
    private UserRepositoryPortOut userRepositoryPortOut;
    private RefreshAuthUseCase refreshAuthUseCase;

    @BeforeEach
    void setUp() {
        tokenService = mock(TokenService.class);
        userRepositoryPortOut = mock(UserRepositoryPortOut.class);
        refreshAuthUseCase = new RefreshAuthUseCase(tokenService, userRepositoryPortOut);
    }

    @Test
    void shouldRefreshTokens() {
        UUID userId = UUID.randomUUID();
        User user = new User(userId, "email", "username");
        when(userRepositoryPortOut.findById(userId)).thenReturn(Optional.of(user));
        when(tokenService.generateAccessToken(anyString(), anyString())).thenReturn("access");
        when(tokenService.generateRefreshToken(anyString())).thenReturn("refresh");

        var response = refreshAuthUseCase.execute(userId);
        assertThat(response.accessToken()).isEqualTo("access");
        assertThat(response.refreshToken()).isEqualTo("refresh");
    }

    @Test
    void shouldThrowWhenUserNotFound() {
        UUID userId = UUID.randomUUID();
        when(userRepositoryPortOut.findById(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> refreshAuthUseCase.execute(userId))
            .isInstanceOf(UserNotFoundException.class);
    }
}
