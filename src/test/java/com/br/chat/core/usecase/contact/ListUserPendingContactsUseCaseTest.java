package com.br.chat.core.usecase.contact;

import com.br.chat.core.domain.contact.Contact;
import com.br.chat.core.domain.contact.ContactRequestStatus;
import com.br.chat.core.domain.user.User;
import com.br.chat.core.port.out.ContactRepositoryPortOut;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListUserPendingContactsUseCaseTest {

    @Mock
    private ContactRepositoryPortOut contactRepositoryPortOut;

    @InjectMocks
    private ListUserPendingContactsUseCase listUserPendingContactsUseCase;

    @Test
    void shouldReturnPendingContacts() {
        var userId = UUID.randomUUID();
        var contact = new Contact();
        contact.setStatus(ContactRequestStatus.PENDING);
        var requestedUser = new User();
        requestedUser.setId(userId);
        contact.setRequestedUser(requestedUser);
        contact.setRequesterUser(requestedUser);

        when(contactRepositoryPortOut.findAllByRequestedId(userId)).thenReturn(List.of(contact));

        var result = listUserPendingContactsUseCase.execute(userId);
        assertThat(result).hasSize(1);
    }
}
