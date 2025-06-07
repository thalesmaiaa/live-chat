package com.br.chat.adapter.out.persistence.user;

import com.br.chat.core.domain.user.User;
import com.br.chat.core.port.out.UserRepositoryPortOut;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserRepositoryAdapterOut implements UserRepositoryPortOut {

    private final UserRepository userRepository;

    @Override
    public void save(User user) {
        var entity = UserEntity.fromDomain(user);
        userRepository.save(entity);
        entity.toDomain();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email).map(UserEntity::toDomain);
    }

    @Override
    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public void deleteById(UUID id) {
        userRepository.deleteById(id);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return userRepository.findById(id).map(UserEntity::toDomain);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll().stream()
                .map(UserEntity::toDomain)
                .toList();
    }

    @Override
    public List<User> findAllById(List<UUID> ids) {
        return userRepository.findAllById(ids).stream()
                .map(UserEntity::toDomain)
                .toList();
    }
}