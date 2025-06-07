package com.br.chat.adapter.in.controller;

import com.br.chat.adapter.in.dto.requests.CreateUserRequest;
import com.br.chat.adapter.in.dto.responses.UserResponse;
import com.br.chat.core.port.in.user.CreateUserPortIn;
import com.br.chat.core.port.in.user.DeleteUserPortIn;
import com.br.chat.core.port.in.user.ListUserPortIn;
import com.br.chat.core.utils.JwtUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserControllerAdapterIn {

    private final CreateUserPortIn createUserPortIn;
    private final DeleteUserPortIn deleteUserPortIn;
    private final ListUserPortIn listUserPortIn;

    @GetMapping("/me")
    public UserResponse findAuthenticatedUser(JwtAuthenticationToken token) {
        return listUserPortIn.execute(JwtUtils.extractUserIdFromToken(token));
    }

    @GetMapping
    public List<UserResponse> findAllUsers() {
        return listUserPortIn.execute();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createUser(@RequestBody @Valid CreateUserRequest request) {
        var user = request.toDomain();
        createUserPortIn.execute(user);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable UUID id) {
        deleteUserPortIn.execute(id);
    }
}
