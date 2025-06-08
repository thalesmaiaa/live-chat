package com.br.chat.core.utils;

import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class JwtUtilsTest {

    @Test
    void shouldExtractUserIdFromToken() {
        UUID userId = UUID.randomUUID();
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("sub", userId.toString());

        JwtAuthenticationToken token = mock(JwtAuthenticationToken.class);
        when(token.getTokenAttributes()).thenReturn(attributes);

        UUID result = JwtUtils.extractUserIdFromToken(token);
        assertThat(result).isEqualTo(userId);
    }
}
