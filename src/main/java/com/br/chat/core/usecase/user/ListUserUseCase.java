package com.br.chat.core.usecase.user;

import com.br.chat.adapter.in.dto.responses.UserResponse;
import com.br.chat.core.domain.user.User;
import com.br.chat.core.exception.UserNotFoundException;
import com.br.chat.core.port.in.user.ListUserPortIn;
import com.br.chat.core.port.out.UserRepositoryPortOut;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ListUserUseCase implements ListUserPortIn {

    private final UserRepositoryPortOut userRepositoryPortOut;

    @Override
    public List<UserResponse> execute() {
        return userRepositoryPortOut.findAll()
                .stream().map(User::toUserResponse)
                .toList();
    }

    @Override
    public UserResponse execute(UUID id) {
        return userRepositoryPortOut.findById(id)
                .map(User::toUserResponse)
                .orElseThrow(UserNotFoundException::new);
    }
}
