package com.br.chat.adapter.in.controller;

import com.br.chat.adapter.in.dto.requests.LoginRequest;
import com.br.chat.adapter.in.dto.responses.AuthResponse;
import com.br.chat.core.port.in.auth.LoginPortIn;
import com.br.chat.core.port.in.auth.RefreshAuthPortIn;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
        var userId = UUID.randomUUID();
        var response = new AuthResponse("access", "refresh");
        var token = Mockito.mock(JwtAuthenticationToken.class);

        when(token.getTokenAttributes()).thenReturn(Map.of("sub", userId));
        when(refreshAuthPortIn.execute(userId)).thenReturn(response);
        var result = controller.refresh(token);
        assertThat(result).isEqualTo(response);
    }
}
