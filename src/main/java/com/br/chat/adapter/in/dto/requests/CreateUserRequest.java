package com.br.chat.adapter.in.dto.requests;

import com.br.chat.core.domain.user.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateUserRequest(@NotBlank @Email String email, @NotBlank String password, @NotBlank String username) {

    public User toDomain() {
        return new User(email, password, username);
    }
}
