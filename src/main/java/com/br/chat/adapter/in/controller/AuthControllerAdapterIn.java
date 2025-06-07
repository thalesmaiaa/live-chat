package com.br.chat.adapter.in.controller;

import com.br.chat.adapter.in.dto.requests.LoginRequest;
import com.br.chat.adapter.in.dto.responses.LoginResponse;
import com.br.chat.core.port.in.auth.LoginPortIn;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/oauth")
public class AuthControllerAdapterIn {

    private final LoginPortIn loginPortIn;

    @PostMapping("/login")
    public LoginResponse login(@RequestBody @Valid LoginRequest request) {
        return loginPortIn.execute(request);
    }
}