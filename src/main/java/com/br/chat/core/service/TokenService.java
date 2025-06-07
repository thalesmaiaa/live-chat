package com.br.chat.core.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class TokenService {

    private static final Long ACCESS_TOKEN_EXPIRATION = 300L;
    private static final Long REFRESH_TOKEN_EXPIRATION = 604800L; // 7 days in seconds
    private static final String tokenIssuer = "chat-live";

    private final JwtEncoder jwtEncoder;

    public String generateAccessToken(String subject, String email) {
        var claim = JwtClaimsSet.builder()
                .subject(subject)
                .claim("email", email)
                .issuer(tokenIssuer)
                .expiresAt(Instant.now().plusSeconds(ACCESS_TOKEN_EXPIRATION))
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claim)).getTokenValue();
    }

    public String generateRefreshToken(String subject) {
        var builder = JwtClaimsSet.builder()
                .subject(subject)
                .claim("type", "refresh")
                .issuer(tokenIssuer)
                .expiresAt(Instant.now().plusSeconds(REFRESH_TOKEN_EXPIRATION));
        JwtClaimsSet claimsSet = builder.build();
        return jwtEncoder.encode(JwtEncoderParameters.from(claimsSet)).getTokenValue();
    }
}