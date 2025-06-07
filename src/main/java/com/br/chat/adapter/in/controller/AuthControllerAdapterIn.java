package com.br.chat.adapter.in.controller;

import com.br.chat.adapter.in.dto.requests.LoginRequest;
import com.br.chat.adapter.in.dto.responses.AuthResponse;
import com.br.chat.core.port.in.auth.LoginPortIn;
import com.br.chat.core.port.in.auth.RefreshAuthPortIn;
import com.br.chat.core.utils.JwtUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/oauth")
public class AuthControllerAdapterIn {

    private final LoginPortIn loginPortIn;
    private final RefreshAuthPortIn refreshAuthPortIn;

    @PostMapping("/login")
    public AuthResponse login(@RequestBody @Valid LoginRequest request) {
        return loginPortIn.execute(request);
    }

    @PostMapping("/refresh")
    public AuthResponse refresh(JwtAuthenticationToken refreshToken) {
        return refreshAuthPortIn.execute(JwtUtils.extractUserIdFromToken(refreshToken));
    }
}