package com.br.chat.core.usecase.auth;

import com.br.chat.adapter.in.dto.requests.LoginRequest;
import com.br.chat.adapter.in.dto.responses.LoginResponse;
import com.br.chat.core.exception.LoginException;
import com.br.chat.core.port.in.auth.LoginPortIn;
import com.br.chat.core.port.out.UserRepositoryPortOut;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class LoginUseCase implements LoginPortIn {

    private final UserRepositoryPortOut userRepositoryPortOut;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtEncoder jwtEncoder;

    @Override
    public LoginResponse execute(LoginRequest request) {
        var user = userRepositoryPortOut.findByEmail(request.email()).orElseThrow(LoginException::new);

        var isPasswordValid = passwordEncoder.matches(request.password(), user.getPassword());

        if (!isPasswordValid) throw new LoginException();

        var expiresIn = 300L;
        var claims = JwtClaimsSet.builder()
                .subject(user.getId().toString())
                .issuer("chat-live")
                .claim("email", user.getEmail())
                .expiresAt(Instant.now().plusSeconds(expiresIn))
                .build();

        var tokenJwt = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        return new LoginResponse(tokenJwt, expiresIn);
    }
}
