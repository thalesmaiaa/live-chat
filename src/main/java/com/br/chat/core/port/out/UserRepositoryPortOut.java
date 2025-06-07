package com.br.chat.core.port.out;

import com.br.chat.core.domain.user.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepositoryPortOut {

    void save(User user);

    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);

    void deleteById(UUID id);

    Optional<User> findById(UUID id);

    List<User> findAll();

    List<User> findAllById(List<UUID> ids);
}