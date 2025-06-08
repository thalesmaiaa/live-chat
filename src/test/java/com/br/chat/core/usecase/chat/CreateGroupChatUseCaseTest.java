package com.br.chat.core.usecase.chat;

import com.br.chat.adapter.in.dto.requests.CreateChatGroupRequest;
import com.br.chat.core.domain.user.User;
import com.br.chat.core.port.out.ChatRepositoryPortOut;
import com.br.chat.core.port.out.UserRepositoryPortOut;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class CreateGroupChatUseCaseTest {

    private ChatRepositoryPortOut chatRepositoryPortOut;
    private UserRepositoryPortOut userRepositoryPortOut;
    private CreateGroupChatUseCase createGroupChatUseCase;

    @BeforeEach
    void setUp() {
        chatRepositoryPortOut = mock(ChatRepositoryPortOut.class);
        userRepositoryPortOut = mock(UserRepositoryPortOut.class);
        createGroupChatUseCase = new CreateGroupChatUseCase(chatRepositoryPortOut, userRepositoryPortOut);
    }

    @Test
    void shouldCreateGroupChat() {
        UUID ownerId = UUID.randomUUID();
        UUID memberId = UUID.randomUUID();
        CreateChatGroupRequest request = new CreateChatGroupRequest(ownerId, List.of(memberId), "group");
        User owner = new User(ownerId, "owner@email.com", "owner");
        User member = new User(memberId, "member@email.com", "member");

        when(userRepositoryPortOut.findById(ownerId)).thenReturn(Optional.of(owner));
        when(userRepositoryPortOut.findAllById(List.of(memberId))).thenReturn(List.of(member));

        assertThatCode(() -> createGroupChatUseCase.execute(request)).doesNotThrowAnyException();
        verify(chatRepositoryPortOut).createGroupChat(any());
    }

    @Test
    void shouldThrowIfMissingMembers() {
        UUID ownerId = UUID.randomUUID();
        UUID memberId = UUID.randomUUID();
        CreateChatGroupRequest request = new CreateChatGroupRequest(ownerId, List.of(memberId), "group");
        User owner = new User(ownerId, "owner@email.com", "owner");

        when(userRepositoryPortOut.findById(ownerId)).thenReturn(Optional.of(owner));
        when(userRepositoryPortOut.findAllById(List.of(memberId))).thenReturn(List.of());

        assertThatThrownBy(() -> createGroupChatUseCase.execute(request))
            .isInstanceOf(RuntimeException.class);
    }
}
