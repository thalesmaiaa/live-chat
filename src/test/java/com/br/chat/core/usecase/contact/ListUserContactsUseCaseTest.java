package com.br.chat.core.usecase.contact;

import com.br.chat.core.domain.chat.Chat;
import com.br.chat.core.domain.chat.ChatType;
import com.br.chat.core.domain.contact.Contact;
import com.br.chat.core.domain.user.User;
import com.br.chat.core.port.out.ChatRepositoryPortOut;
import com.br.chat.core.port.out.ContactRepositoryPortOut;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListUserContactsUseCaseTest {

    @Mock
    private ContactRepositoryPortOut contactRepositoryPortOut;

    @Mock
    private ChatRepositoryPortOut chatRepositoryPortOut;

    @InjectMocks
    private ListUserContactsUseCase listUserContactsUseCase;

    private UUID userId;

    @Test
    void shouldListUserEmptyContacts() {
        when(contactRepositoryPortOut.findAllAcceptedContactsByUserId(userId)).thenReturn(List.of());

        var result = listUserContactsUseCase.execute(userId);

        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
        verify(contactRepositoryPortOut).findAllAcceptedContactsByUserId(userId);
    }

    @Test
    void shouldListUserContacts() {
        var firstChatUser = new User();
        firstChatUser.setId(userId);
        var secondChatUser = new User();
        secondChatUser.setId(UUID.randomUUID());
        var contact = new Contact();
        contact.setId(1L);
        contact.setRequesterUser(firstChatUser);
        contact.setRequestedUser(secondChatUser);

        var chat = new Chat();
        chat.setId(UUID.randomUUID());
        chat.setType(ChatType.PRIVATE);
        chat.setUsers(List.of(secondChatUser));

        when(contactRepositoryPortOut.findAllAcceptedContactsByUserId(userId))
                .thenReturn(List.of(contact));
        when(chatRepositoryPortOut.findAllByUserId(userId))
                .thenReturn(List.of(chat));

        var result = listUserContactsUseCase.execute(userId);

        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(1);
        verify(contactRepositoryPortOut).findAllAcceptedContactsByUserId(userId);
        verify(chatRepositoryPortOut).findAllByUserId(userId);
    }

    @Test
    void shouldListUserContactsWhenUserIsSender() {
        var firstChatUser = new User();
        firstChatUser.setId(userId);
        var secondChatUser = new User();
        secondChatUser.setId(UUID.randomUUID());
        var contact = new Contact();
        contact.setId(1L);
        contact.setRequesterUser(firstChatUser);
        contact.setRequestedUser(secondChatUser);

        var chat = new Chat();
        chat.setId(UUID.randomUUID());
        chat.setType(ChatType.PRIVATE);
        chat.setUsers(List.of(firstChatUser));

        when(contactRepositoryPortOut.findAllAcceptedContactsByUserId(secondChatUser.getId()))
                .thenReturn(List.of(contact));
        when(chatRepositoryPortOut.findAllByUserId(secondChatUser.getId()))
                .thenReturn(List.of(chat));

        var result = listUserContactsUseCase.execute(secondChatUser.getId());

        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(1);
        verify(contactRepositoryPortOut).findAllAcceptedContactsByUserId(secondChatUser.getId());
        verify(chatRepositoryPortOut).findAllByUserId(secondChatUser.getId());
    }
}