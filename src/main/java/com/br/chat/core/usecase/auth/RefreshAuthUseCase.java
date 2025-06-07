package com.br.chat.core.usecase.auth;

import com.br.chat.adapter.in.dto.responses.AuthResponse;
import com.br.chat.core.exception.UserNotFoundException;
import com.br.chat.core.port.in.auth.RefreshAuthPortIn;
import com.br.chat.core.port.out.UserRepositoryPortOut;
import com.br.chat.core.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class RefreshAuthUseCase implements RefreshAuthPortIn {

    private final TokenService tokenService;
    private final UserRepositoryPortOut userRepositoryPortOut;

    @Override
    public AuthResponse execute(UUID userId) {
        var user = userRepositoryPortOut.findById(userId).orElseThrow(UserNotFoundException::new);

        var newAccessToken = tokenService.generateAccessToken(user.getId().toString(), user.getEmail());
        var newRefreshToken = tokenService.generateRefreshToken(user.getId().toString());

        return new AuthResponse(newAccessToken, newRefreshToken);
    }
}