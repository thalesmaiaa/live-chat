package com.br.chat.core.usecase.contact;

import com.br.chat.core.port.out.ContactRepositoryPortOut;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class RemoveContactUseCaseTest {

    private ContactRepositoryPortOut contactRepositoryPortOut;
    private RemoveContactUseCase removeContactUseCase;

    @BeforeEach
    void setUp() {
        contactRepositoryPortOut = mock(ContactRepositoryPortOut.class);
        removeContactUseCase = new RemoveContactUseCase(contactRepositoryPortOut);
    }

    @Test
    void shouldRemoveContactById() {
        removeContactUseCase.execute(1L);
        verify(contactRepositoryPortOut).deleteById(1L);
    }
}
