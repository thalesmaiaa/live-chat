package com.br.chat.core.usecase.contact;

import com.br.chat.core.port.in.contact.RemoveContactPortIn;
import com.br.chat.core.port.out.ContactRepositoryPortOut;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RemoveContactUseCase implements RemoveContactPortIn {

    private final ContactRepositoryPortOut contactRepositoryPortOut;

    @Override
    public void execute(Long id) {
        contactRepositoryPortOut.deleteById(id);
    }
}
