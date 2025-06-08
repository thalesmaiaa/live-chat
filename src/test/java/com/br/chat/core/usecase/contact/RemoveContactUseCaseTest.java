package com.br.chat.core.usecase.contact;

import com.br.chat.core.port.out.ContactRepositoryPortOut;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RemoveContactUseCaseTest {

    @Mock
    private ContactRepositoryPortOut contactRepositoryPortOut;

    @InjectMocks
    private RemoveContactUseCase removeContactUseCase;

    @Test
    void shouldRemoveContactById() {
        removeContactUseCase.execute(1L);
        verify(contactRepositoryPortOut).deleteById(1L);
    }
}
