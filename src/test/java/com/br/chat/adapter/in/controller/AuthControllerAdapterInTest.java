package com.br.chat.adapter.in.controller;

import com.br.chat.adapter.in.dto.requests.LoginRequest;
import com.br.chat.adapter.in.dto.responses.AuthResponse;
import com.br.chat.core.port.in.auth.LoginPortIn;
import com.br.chat.core.port.in.auth.RefreshAuthPortIn;
import com.br.chat.core.utils.JwtUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerAdapterInTest {

    @Mock
    private LoginPortIn loginPortIn;
    @Mock
    private RefreshAuthPortIn refreshAuthPortIn;

    @InjectMocks
    private AuthControllerAdapterIn controller;

    @Test
    void shouldLoginReturnAuthResponse() {
        var request = mock(LoginRequest.class);
        var response = new AuthResponse("access", "refresh");
        when(loginPortIn.execute(request)).thenReturn(response);
        var result = controller.login(request);
        assertThat(result).isEqualTo(response);
    }

    @Test
    void shouldRefreshReturnAuthResponse() {
        var token = mock(JwtAuthenticationToken.class);
        var userId = UUID.randomUUID();
        var response = new AuthResponse("access", "refresh");
        when(refreshAuthPortIn.execute(userId)).thenReturn(response);
        try (MockedStatic<JwtUtils> jwtUtils = Mockito.mockStatic(JwtUtils.class)) {
            jwtUtils.when(() -> JwtUtils.extractUserIdFromToken(token)).thenReturn(userId);
            var result = controller.refresh(token);
            assertThat(result).isEqualTo(response);
        }
    }
}
