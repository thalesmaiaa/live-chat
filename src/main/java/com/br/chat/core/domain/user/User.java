package com.br.chat.core.domain.user;

import com.br.chat.adapter.in.dto.responses.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.ZonedDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private UUID id;
    private String username;
    private String email;
    private String password;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;

    public User(String email, String password, String username) {
        this.email = email;
        this.password = password;
        this.username = username;
    }

    public User(UUID id, String email, String username) {
        this.id = id;
        this.email = email;
        this.username = username;
    }

    public static UserResponse toUserResponse(User user) {
        return new UserResponse(user.getId(), user.getEmail(), user.getUsername(), user.getCreatedAt(), user.getUpdatedAt());
    }

    public void encodePassword(BCryptPasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(this.password);
    }

}
