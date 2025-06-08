package com.br.chat.core.usecase.chat;

import com.br.chat.adapter.in.dto.requests.CreateChatGroupRequest;
import com.br.chat.core.domain.user.User;
import com.br.chat.core.port.out.ChatRepositoryPortOut;
import com.br.chat.core.port.out.UserRepositoryPortOut;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateGroupChatUseCaseTest {

    @Mock
    private ChatRepositoryPortOut chatRepositoryPortOut;
    @Mock
    private UserRepositoryPortOut userRepositoryPortOut;

    @InjectMocks
    private CreateGroupChatUseCase createGroupChatUseCase;

    @Test
    void shouldCreateGroupChat() {
        var ownerId = UUID.randomUUID();
        var memberId = UUID.randomUUID();
        var request = new CreateChatGroupRequest(ownerId, List.of(memberId), "group");
        var owner = new User(ownerId, "owner@email.com", "owner");
        var member = new User(memberId, "member@email.com", "member");

        when(userRepositoryPortOut.findById(ownerId)).thenReturn(Optional.of(owner));
        when(userRepositoryPortOut.findAllById(List.of(memberId))).thenReturn(List.of(member));

        assertThatCode(() -> createGroupChatUseCase.execute(request)).doesNotThrowAnyException();
        verify(chatRepositoryPortOut).createGroupChat(any());
    }

    @Test
    void shouldThrowIfMissingMembers() {
        var ownerId = UUID.randomUUID();
        var memberId = UUID.randomUUID();
        var request = new CreateChatGroupRequest(ownerId, List.of(memberId), "group");
        var owner = new User(ownerId, "owner@email.com", "owner");

        when(userRepositoryPortOut.findById(ownerId)).thenReturn(Optional.of(owner));
        when(userRepositoryPortOut.findAllById(List.of(memberId))).thenReturn(List.of());

        assertThatThrownBy(() -> createGroupChatUseCase.execute(request))
            .isInstanceOf(RuntimeException.class);
    }
}
