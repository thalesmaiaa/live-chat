package com.br.chat.adapter.out.persistence.contact;

import com.br.chat.core.domain.contact.Contact;
import com.br.chat.core.domain.contact.ContactRequestStatus;
import com.br.chat.core.domain.user.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContactRepositoryAdapterOutTest {

    @Mock
    ContactRepository contactRepository;

    @InjectMocks
    ContactRepositoryAdapterOut adapter;

    @Test
    void shouldReturnContactsWhenFindAllByRequestedId() {
        var userId = UUID.randomUUID();
        var entity = mock(ContactEntity.class);
        var contact = mock(Contact.class);
        when(entity.toDomain()).thenReturn(contact);
        when(contactRepository.findAllByRequestedUserId(userId)).thenReturn(List.of(entity));
        var result = adapter.findAllByRequestedId(userId);
        assertThat(result).containsExactly(contact);
    }

    @Test
    void shouldCallRepositorySaveWhenSave() {
        var contact = mock(Contact.class);
        var requesterUser = mock(User.class);
        var requestedUser = mock(User.class);
        when(requesterUser.getId()).thenReturn(UUID.randomUUID());
        when(requestedUser.getId()).thenReturn(UUID.randomUUID());
        when(contact.getRequesterUser()).thenReturn(requesterUser);
        when(contact.getRequestedUser()).thenReturn(requestedUser);
        when(contact.getStatus()).thenReturn(ContactRequestStatus.ACCEPTED);
        var entity = mock(ContactEntity.class);
        when(contactRepository.save(any(ContactEntity.class))).thenReturn(entity);
        adapter.save(contact);
        verify(contactRepository).save(any());
    }

    @Test
    void shouldReturnContactWhenFindById() {
        var id = 1L;
        var contact = mock(Contact.class);
        var entity = mock(ContactEntity.class);
        when(entity.toDomain()).thenReturn(contact);
        when(contactRepository.findById(id)).thenReturn(Optional.of(entity));
        var result = adapter.findById(id);
        assertThat(result).contains(contact);
    }

    @Test
    void shouldReturnBooleanWhenExistsActiveRequestsByRequesterIdAndRequestedId() {
        var requesterId = UUID.randomUUID();
        var requestedId = UUID.randomUUID();
        when(contactRepository.existsActiveRequestsByRequesterIdAndRequestedId(requesterId, requestedId)).thenReturn(true);
        var result = adapter.existsActiveRequestsByRequesterIdAndRequestedId(requesterId, requestedId);
        assertThat(result).isTrue();
    }

    @Test
    void shouldReturnAcceptedContactsWhenFindAllAcceptedContactsByUserId() {
        var userId = UUID.randomUUID();
        var contact = mock(Contact.class);
        when(contact.isAccepted()).thenReturn(true);
        var entity = mock(ContactEntity.class);
        when(entity.toDomain()).thenReturn(contact);
        when(contactRepository.findAllByUserId(userId)).thenReturn(List.of(entity));
        var result = adapter.findAllAcceptedContactsByUserId(userId);
        assertThat(result).containsExactly(contact);
    }

    @Test
    void shouldCallRepositoryWhenDeleteById() {
        var id = 1L;
        adapter.deleteById(id);
        verify(contactRepository).deleteById(id);
    }
}
