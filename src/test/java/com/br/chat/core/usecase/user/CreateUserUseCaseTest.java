package com.br.chat.core.usecase.user;

import com.br.chat.core.domain.user.User;
import com.br.chat.core.exception.UserAlreadyExistException;
import com.br.chat.core.port.out.UserRepositoryPortOut;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateUserUseCaseTest {

    @Mock
    private UserRepositoryPortOut userRepositoryPortOut;
    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private CreateUserUseCase createUserUseCase;

    @Test
    void shouldCreateUserWhenEmailNotTaken() {
        var user = new User("test@email.com", "password", "username");
        when(userRepositoryPortOut.existsByEmail(user.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        assertThatCode(() -> createUserUseCase.execute(user)).doesNotThrowAnyException();
        verify(userRepositoryPortOut).save(user);
    }

    @Test
    void shouldThrowWhenEmailAlreadyExists() {
        var user = new User("test@email.com", "password", "username");
        when(userRepositoryPortOut.existsByEmail(user.getEmail())).thenReturn(true);

        assertThatThrownBy(() -> createUserUseCase.execute(user))
            .isInstanceOf(UserAlreadyExistException.class);
        verify(userRepositoryPortOut, never()).save(any());
    }
}
