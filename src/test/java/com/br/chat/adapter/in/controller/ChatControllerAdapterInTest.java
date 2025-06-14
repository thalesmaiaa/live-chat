package com.br.chat.adapter.in.controller;

import com.br.chat.adapter.in.dto.requests.CreateChatGroupRequest;
import com.br.chat.adapter.in.dto.requests.CreatePrivateChatRequest;
import com.br.chat.adapter.in.dto.responses.ChatResponse;
import com.br.chat.core.port.in.chat.CreateGroupChatPortIn;
import com.br.chat.core.port.in.chat.CreatePrivateChatPortIn;
import com.br.chat.core.port.in.chat.ListChatPortIn;
import com.br.chat.core.port.in.chat.ListUserChatPortIn;
import com.br.chat.core.utils.JwtUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChatControllerAdapterInTest {

    @Mock
    private ListChatPortIn listChatPortIn;
    @Mock
    private ListUserChatPortIn listUserChatPortIn;
    @Mock
    private CreateGroupChatPortIn createGroupChatPortIn;
    @Mock
    private CreatePrivateChatPortIn createPrivateChatPortIn;

    @InjectMocks
    private ChatControllerAdapterIn controller;

    @Test
    void shouldCreateGroupChatCallPort() {
        var request = mock(CreateChatGroupRequest.class);
        controller.createGroupChat(request);
        verify(createGroupChatPortIn).execute(request);
    }

    @Test
    void shouldFindAllUserChatsReturnChats() {
        var token = mock(JwtAuthenticationToken.class);
        var userId = UUID.randomUUID();
        var chats = List.of(mock(ChatResponse.class));
        when(listUserChatPortIn.execute(userId)).thenReturn(chats);
        try (MockedStatic<JwtUtils> jwtUtils = Mockito.mockStatic(JwtUtils.class)) {
            jwtUtils.when(() -> JwtUtils.extractUserIdFromToken(token)).thenReturn(userId);
            var result = controller.findAllUserChats(token);
            assertThat(result).isEqualTo(chats);
        }
    }

    @Test
    void shouldFindChatByIdReturnChat() {
        var chatId = UUID.randomUUID();
        var chat = mock(ChatResponse.class);
        when(listChatPortIn.execute(chatId)).thenReturn(chat);
        var result = controller.findChatById(chatId);
        assertThat(result).isEqualTo(chat);
    }

    @Test
    void shouldStartPrivateChatCallPortAndReturnId() {
        var senderId = UUID.randomUUID();
        var request = mock(CreatePrivateChatRequest.class);
        var chatId = UUID.randomUUID();
        when(createPrivateChatPortIn.execute(request)).thenReturn(chatId);
        var result = controller.startPrivateChat(senderId, request);
        verify(request).setSenderId(senderId);
        assertThat(result).isEqualTo(chatId);
    }
}
