package com.br.chat.core.usecase.contact;

import com.br.chat.adapter.in.dto.requests.UpdateContactRequestStatus;
import com.br.chat.core.domain.contact.Contact;
import com.br.chat.core.domain.contact.ContactRequestStatus;
import com.br.chat.core.port.out.ContactRepositoryPortOut;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateContactRequestUseCaseTest {

    @Mock
    private ContactRepositoryPortOut contactRepositoryPortOut;

    @InjectMocks
    private UpdateContactRequestUseCase updateContactRequestUseCase;

    @Test
    void shouldUpdateContactStatus() {
        var contact = new Contact();
        when(contactRepositoryPortOut.findById(1L)).thenReturn(Optional.of(contact));
        var request = new UpdateContactRequestStatus(1L, ContactRequestStatus.ACCEPTED);

        updateContactRequestUseCase.execute(request);

        verify(contactRepositoryPortOut).save(contact);
        assert(contact.getStatus() == ContactRequestStatus.ACCEPTED);
    }
}
