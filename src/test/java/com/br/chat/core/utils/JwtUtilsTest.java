package com.br.chat.core.utils;

import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.HashMap;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class JwtUtilsTest {

    @Test
    void shouldExtractUserIdFromToken() {
        var userId = UUID.randomUUID();
        var attributes = new HashMap<String, Object>();
        attributes.put("sub", userId.toString());

        var token = mock(JwtAuthenticationToken.class);
        when(token.getTokenAttributes()).thenReturn(attributes);

        var result = JwtUtils.extractUserIdFromToken(token);
        assertThat(result).isEqualTo(userId);
    }
}
