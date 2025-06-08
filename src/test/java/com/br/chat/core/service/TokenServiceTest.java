package com.br.chat.core.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.Jwt;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TokenServiceTest {

    private JwtEncoder jwtEncoder;
    private TokenService tokenService;

    @BeforeEach
    void setUp() {
        jwtEncoder = mock(JwtEncoder.class);
        tokenService = new TokenService(jwtEncoder);
    }

    @Test
    void shouldGenerateAccessToken() {
        Jwt jwt = mock(Jwt.class);
        when(jwt.getTokenValue()).thenReturn("mocked-access-token");
        when(jwtEncoder.encode(any(JwtEncoderParameters.class))).thenReturn(jwt);
        assertThat(tokenService.generateAccessToken("sub", "email")).isNotNull();
    }

    @Test
    void shouldGenerateRefreshToken() {
        Jwt jwt = mock(Jwt.class);
        when(jwt.getTokenValue()).thenReturn("mocked-refresh-token");
        when(jwtEncoder.encode(any(JwtEncoderParameters.class))).thenReturn(jwt);
        assertThat(tokenService.generateRefreshToken("sub")).isNotNull();
    }
}
