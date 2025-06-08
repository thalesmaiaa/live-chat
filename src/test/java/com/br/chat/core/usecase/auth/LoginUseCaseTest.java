package com.br.chat.core.usecase.auth;

import com.br.chat.adapter.in.dto.requests.LoginRequest;
import com.br.chat.core.domain.user.User;
import com.br.chat.core.exception.LoginException;
import com.br.chat.core.port.out.UserRepositoryPortOut;
import com.br.chat.core.service.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class LoginUseCaseTest {

    private UserRepositoryPortOut userRepositoryPortOut;
    private BCryptPasswordEncoder passwordEncoder;
    private TokenService tokenService;
    private LoginUseCase loginUseCase;

    @BeforeEach
    void setUp() {
        userRepositoryPortOut = mock(UserRepositoryPortOut.class);
        passwordEncoder = mock(BCryptPasswordEncoder.class);
        tokenService = mock(TokenService.class);
        loginUseCase = new LoginUseCase(userRepositoryPortOut, passwordEncoder, tokenService);
    }

    @Test
    void shouldLoginSuccessfully() {
        LoginRequest request = new LoginRequest("test@email.com", "password");
        User user = new User(UUID.randomUUID(), "test@email.com", "username");
        user.setPassword("hashed");
        when(userRepositoryPortOut.findByEmail(request.email())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(request.password(), user.getPassword())).thenReturn(true);
        when(tokenService.generateAccessToken(anyString(), anyString())).thenReturn("access");
        when(tokenService.generateRefreshToken(anyString())).thenReturn("refresh");

        var response = loginUseCase.execute(request);
        assertThat(response.accessToken()).isEqualTo("access");
        assertThat(response.refreshToken()).isEqualTo("refresh");
    }

    @Test
    void shouldThrowWhenUserNotFound() {
        LoginRequest request = new LoginRequest("notfound@email.com", "password");
        when(userRepositoryPortOut.findByEmail(request.email())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> loginUseCase.execute(request))
            .isInstanceOf(LoginException.class);
    }

    @Test
    void shouldThrowWhenPasswordInvalid() {
        LoginRequest request = new LoginRequest("test@email.com", "password");
        User user = new User(UUID.randomUUID(), "test@email.com", "username");
        user.setPassword("hashed");
        when(userRepositoryPortOut.findByEmail(request.email())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(request.password(), user.getPassword())).thenReturn(false);

        assertThatThrownBy(() -> loginUseCase.execute(request))
            .isInstanceOf(LoginException.class);
    }
}
