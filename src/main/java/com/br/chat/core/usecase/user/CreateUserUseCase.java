package com.br.chat.core.usecase.user;


import com.br.chat.core.domain.user.User;
import com.br.chat.core.exception.UserAlreadyExistException;
import com.br.chat.core.port.in.user.CreateUserPortIn;
import com.br.chat.core.port.out.UserRepositoryPortOut;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateUserUseCase implements CreateUserPortIn {

    private final UserRepositoryPortOut userRepositoryPortOut;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public void execute(User user) {
        var isEmailTaken = userRepositoryPortOut.existsByEmail(user.getEmail());

        if (isEmailTaken) throw new UserAlreadyExistException();

        user.encodePassword(passwordEncoder);

        userRepositoryPortOut.save(user);
    }
}
