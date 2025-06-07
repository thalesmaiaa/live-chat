package com.br.chat.core.usecase.auth;

import com.br.chat.adapter.in.dto.requests.LoginRequest;
import com.br.chat.adapter.in.dto.responses.AuthResponse;
import com.br.chat.core.exception.LoginException;
import com.br.chat.core.port.in.auth.LoginPortIn;
import com.br.chat.core.port.out.UserRepositoryPortOut;
import com.br.chat.core.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LoginUseCase implements LoginPortIn {

    private final UserRepositoryPortOut userRepositoryPortOut;
    private final BCryptPasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    @Override
    public AuthResponse execute(LoginRequest request) {
        var user = userRepositoryPortOut.findByEmail(request.email()).orElseThrow(LoginException::new);

        var isPasswordValid = passwordEncoder.matches(request.password(), user.getPassword());
        if (!isPasswordValid) throw new LoginException();

        var accessToken = tokenService.generateAccessToken(user.getId().toString(), user.getEmail());
        var refreshToken = tokenService.generateRefreshToken(user.getId().toString());

        return new AuthResponse(accessToken, refreshToken);
    }
}
