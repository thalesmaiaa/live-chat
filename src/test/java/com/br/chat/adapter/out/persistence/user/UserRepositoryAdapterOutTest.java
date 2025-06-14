package com.br.chat.adapter.out.persistence.user;

import com.br.chat.core.domain.user.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserRepositoryAdapterOutTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserRepositoryAdapterOut adapter;

    @Test
    void shouldCallRepositorySaveWhenSave() {
        var user = mock(User.class);
        adapter.save(user);
        verify(userRepository).save(any(UserEntity.class));
    }

    @Test
    void shouldReturnUserWhenFindByEmail() {
        var email = "test@email.com";
        var entity = mock(UserEntity.class);
        var user = mock(User.class);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(entity));
        when(entity.toDomain()).thenReturn(user);
        var result = adapter.findByEmail(email);
        assertThat(result).contains(user);
    }

    @Test
    void shouldReturnBooleanWhenExistsByEmail() {
        var email = "test@email.com";
        when(userRepository.existsByEmail(email)).thenReturn(true);
        var result = adapter.existsByEmail(email);
        assertThat(result).isTrue();
    }

    @Test
    void shouldCallRepositoryWhenDeleteById() {
        var id = UUID.randomUUID();
        adapter.deleteById(id);
        verify(userRepository).deleteById(id);
    }

    @Test
    void shouldReturnUserWhenFindById() {
        var id = UUID.randomUUID();
        var entity = mock(UserEntity.class);
        var user = mock(User.class);
        when(userRepository.findById(id)).thenReturn(Optional.of(entity));
        when(entity.toDomain()).thenReturn(user);
        var result = adapter.findById(id);
        assertThat(result).contains(user);
    }

    @Test
    void shouldReturnUsersWhenFindAll() {
        var entity = mock(UserEntity.class);
        var user = mock(User.class);
        when(userRepository.findAll()).thenReturn(List.of(entity));
        when(entity.toDomain()).thenReturn(user);
        var result = adapter.findAll();
        assertThat(result).containsExactly(user);
    }

    @Test
    void shouldReturnUsersWhenFindAllById() {
        var ids = List.of(UUID.randomUUID());
        var entity = mock(UserEntity.class);
        var user = mock(User.class);
        when(userRepository.findAllById(ids)).thenReturn(List.of(entity));
        when(entity.toDomain()).thenReturn(user);
        var result = adapter.findAllById(ids);
        assertThat(result).containsExactly(user);
    }
}
