package com.br.chat.adapter.in.controller;

import com.br.chat.adapter.in.dto.requests.CreateUserRequest;
import com.br.chat.adapter.in.dto.responses.UserResponse;
import com.br.chat.core.port.in.user.CreateUserPortIn;
import com.br.chat.core.port.in.user.DeleteUserPortIn;
import com.br.chat.core.port.in.user.ListUserPortIn;
import com.br.chat.core.utils.JwtUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerAdapterInTest {

    @Mock
    private CreateUserPortIn createUserPortIn;
    @Mock
    private DeleteUserPortIn deleteUserPortIn;
    @Mock
    private ListUserPortIn listUserPortIn;

    @InjectMocks
    private UserControllerAdapterIn controller;

    @Test
    void shouldFindAuthenticatedUserReturnUserResponse() {
        var token = mock(JwtAuthenticationToken.class);
        var userId = UUID.randomUUID();
        var userResponse = new UserResponse(userId, "email", "username", ZonedDateTime.now(), ZonedDateTime.now());
        when(listUserPortIn.execute(any())).thenReturn(userResponse);
        try (MockedStatic<JwtUtils> jwtUtils = Mockito.mockStatic(JwtUtils.class)) {
            jwtUtils.when(() -> JwtUtils.extractUserIdFromToken(token)).thenReturn(userId);
            var result = controller.findAuthenticatedUser(token);
            assertThat(result).isEqualTo(userResponse);
        }
    }

    @Test
    void shouldFindAllUsersReturnListOfUsers() {
        var userList = List.of(
                new UserResponse(UUID.randomUUID(), "email", "username", ZonedDateTime.now(), ZonedDateTime.now())
        );
        when(listUserPortIn.execute()).thenReturn(userList);
        var result = controller.findAllUsers();
        assertThat(result).isEqualTo(userList);
    }

    @Test
    void shouldCreateUserCallPort() {
        var request = mock(CreateUserRequest.class);
        var user = mock(com.br.chat.core.domain.user.User.class);
        when(request.toDomain()).thenReturn(user);
        controller.createUser(request);
        verify(createUserPortIn).execute(user);
    }

    @Test
    void shouldDeleteUserCallPort() {
        var id = UUID.randomUUID();
        controller.deleteUser(id);
        verify(deleteUserPortIn).execute(id);
    }
}
