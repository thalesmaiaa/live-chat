package com.br.chat.core.usecase.user;

import com.br.chat.core.domain.user.User;
import com.br.chat.core.exception.UserAlreadyExistException;
import com.br.chat.core.port.out.UserRepositoryPortOut;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class CreateUserUseCaseTest {

    private UserRepositoryPortOut userRepositoryPortOut;
    private BCryptPasswordEncoder passwordEncoder;
    private CreateUserUseCase createUserUseCase;

    @BeforeEach
    void setUp() {
        userRepositoryPortOut = mock(UserRepositoryPortOut.class);
        passwordEncoder = mock(BCryptPasswordEncoder.class);
        createUserUseCase = new CreateUserUseCase(userRepositoryPortOut, passwordEncoder);
    }

    @Test
    void shouldCreateUserWhenEmailNotTaken() {
        User user = new User("test@email.com", "password", "username");
        when(userRepositoryPortOut.existsByEmail(user.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        assertThatCode(() -> createUserUseCase.execute(user)).doesNotThrowAnyException();
        verify(userRepositoryPortOut).save(user);
    }

    @Test
    void shouldThrowWhenEmailAlreadyExists() {
        User user = new User("test@email.com", "password", "username");
        when(userRepositoryPortOut.existsByEmail(user.getEmail())).thenReturn(true);

        assertThatThrownBy(() -> createUserUseCase.execute(user))
            .isInstanceOf(UserAlreadyExistException.class);
        verify(userRepositoryPortOut, never()).save(any());
    }
}
