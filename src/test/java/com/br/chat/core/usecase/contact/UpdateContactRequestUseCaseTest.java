package com.br.chat.core.usecase.contact;

import com.br.chat.adapter.in.dto.requests.UpdateContactRequestStatus;
import com.br.chat.core.domain.contact.Contact;
import com.br.chat.core.domain.contact.ContactRequestStatus;
import com.br.chat.core.port.out.ContactRepositoryPortOut;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.mockito.Mockito.*;

class UpdateContactRequestUseCaseTest {

    private ContactRepositoryPortOut contactRepositoryPortOut;
    private UpdateContactRequestUseCase updateContactRequestUseCase;

    @BeforeEach
    void setUp() {
        contactRepositoryPortOut = mock(ContactRepositoryPortOut.class);
        updateContactRequestUseCase = new UpdateContactRequestUseCase(contactRepositoryPortOut);
    }

    @Test
    void shouldUpdateContactStatus() {
        Contact contact = new Contact();
        when(contactRepositoryPortOut.findById(1L)).thenReturn(Optional.of(contact));
        UpdateContactRequestStatus request = new UpdateContactRequestStatus(1L, ContactRequestStatus.ACCEPTED);

        updateContactRequestUseCase.execute(request);

        verify(contactRepositoryPortOut).save(contact);
        assert(contact.getStatus() == ContactRequestStatus.ACCEPTED);
    }
}
