package com.br.chat.adapter.out.persistence.chat;

import com.br.chat.adapter.out.persistence.message.MessageRepository;
import com.br.chat.adapter.out.persistence.userchat.UserChatRepository;
import com.br.chat.core.domain.chat.Chat;
import com.br.chat.core.domain.chat.ChatType;
import com.br.chat.core.domain.message.Message;
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
class ChatRepositoryAdapterOutTest {

    @Mock
    ChatRepository chatRepository;

    @Mock
    MessageRepository messageRepository;

    @Mock
    UserChatRepository userChatRepository;

    @InjectMocks
    ChatRepositoryAdapterOut adapter;

    @Test
    void shouldSaveEntitiesAndReturnIdWhenStartChat() {
        var chat = mock(Chat.class);
        var savedChat = mock(ChatEntity.class);
        var user = mock(User.class);
        var message = mock(Message.class);
        var chatId = UUID.randomUUID();

        when(user.getId()).thenReturn(UUID.randomUUID());
        when(message.getSenderUser()).thenReturn(user);
        when(chat.getUsers()).thenReturn(List.of(user));
        when(chat.getMessages()).thenReturn(List.of(message));
        when(chatRepository.save(any())).thenReturn(savedChat);
        when(savedChat.getId()).thenReturn(chatId);
        when(chat.getType()).thenReturn(ChatType.PRIVATE);

        var result = adapter.startChat(chat);

        verify(userChatRepository).saveAll(any());
        verify(messageRepository).saveAll(any());
        assertThat(result).isEqualTo(chatId);
    }

    @Test
    void shouldReturnChatWhenFindChatById() {
        var id = UUID.randomUUID();
        var entity = mock(ChatEntity.class);
        var chat = mock(Chat.class);
        when(chatRepository.findById(id)).thenReturn(Optional.of(entity));
        when(entity.toDomain()).thenReturn(chat);

        var result = adapter.findChatById(id);
        assertThat(result).contains(chat);
        verify(chatRepository).findById(id);
    }

    @Test
    void shouldSaveEntitiesWhenCreateGroupChat() {
        var chat = mock(Chat.class);
        var savedChat = mock(ChatEntity.class);
        var user = mock(User.class);

        when(chat.getType()).thenReturn(ChatType.GROUP);

        when(chat.getUsers()).thenReturn(List.of(user));
        when(chat.getOwnerId()).thenReturn(UUID.randomUUID());
        when(user.matchesId(any())).thenReturn(true);
        when(chatRepository.save(any())).thenReturn(savedChat);

        adapter.createGroupChat(chat);
        verify(userChatRepository).saveAll(any());
    }

    @Test
    void shouldReturnChatsWhenFindAllByUserId() {
        var userId = UUID.randomUUID();
        var entity = mock(ChatEntity.class);
        when(chatRepository.findAllByUserId(userId)).thenReturn(List.of(entity));
        var result = adapter.findAllByUserId(userId);
        assertThat(result).hasSize(1);
        verify(chatRepository).findAllByUserId(userId);
    }
}
