package com.br.chat.core.utils;

import lombok.experimental.UtilityClass;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.UUID;

@UtilityClass
public class JwtUtils {

    public static UUID extractUserIdFromToken(JwtAuthenticationToken token) {
        return UUID.fromString(String.valueOf(token.getTokenAttributes().get("sub")));
    }
}
