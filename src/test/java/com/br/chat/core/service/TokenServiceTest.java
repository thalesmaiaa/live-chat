package com.br.chat.core.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.Jwt;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TokenServiceTest {

    @Mock
    private JwtEncoder jwtEncoder;

    @InjectMocks
    private TokenService tokenService;

    @Test
    void shouldGenerateAccessToken() {
        var jwt = mock(Jwt.class);
        when(jwt.getTokenValue()).thenReturn("mocked-access-token");
        when(jwtEncoder.encode(any(JwtEncoderParameters.class))).thenReturn(jwt);
        assertThat(tokenService.generateAccessToken("sub", "email")).isNotNull();
    }

    @Test
    void shouldGenerateRefreshToken() {
        var jwt = mock(Jwt.class);
        when(jwt.getTokenValue()).thenReturn("mocked-refresh-token");
        when(jwtEncoder.encode(any(JwtEncoderParameters.class))).thenReturn(jwt);
        assertThat(tokenService.generateRefreshToken("sub")).isNotNull();
    }
}
