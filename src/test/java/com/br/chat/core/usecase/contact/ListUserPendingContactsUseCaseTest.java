package com.br.chat.core.usecase.contact;

import com.br.chat.core.domain.contact.Contact;
import com.br.chat.core.domain.contact.ContactRequestStatus;
import com.br.chat.core.domain.user.User;
import com.br.chat.core.port.out.ContactRepositoryPortOut;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ListUserPendingContactsUseCaseTest {

    private ContactRepositoryPortOut contactRepositoryPortOut;
    private ListUserPendingContactsUseCase listUserPendingContactsUseCase;

    @BeforeEach
    void setUp() {
        contactRepositoryPortOut = mock(ContactRepositoryPortOut.class);
        listUserPendingContactsUseCase = new ListUserPendingContactsUseCase(contactRepositoryPortOut);
    }

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
