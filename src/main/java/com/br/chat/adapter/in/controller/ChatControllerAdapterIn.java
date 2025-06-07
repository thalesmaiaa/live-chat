package com.br.chat.adapter.in.controller;


import com.br.chat.adapter.in.dto.requests.CreateChatGroupRequest;
import com.br.chat.adapter.in.dto.requests.CreatePrivateChatRequest;
import com.br.chat.adapter.in.dto.responses.ChatResponse;
import com.br.chat.core.port.in.chat.CreateGroupChatPortIn;
import com.br.chat.core.port.in.chat.CreatePrivateChatPortIn;
import com.br.chat.core.port.in.chat.ListChatPortIn;
import com.br.chat.core.port.in.chat.ListUserChatPortIn;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/chats")
public class ChatControllerAdapterIn {

    private final CreateGroupChatPortIn createGroupChatPortIn;
    private final CreatePrivateChatPortIn createPrivateChatPortIn;
    private final ListUserChatPortIn listUserChatPortIn;
    private final ListChatPortIn listChatPortIn;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createGroupChat(@RequestBody @Valid CreateChatGroupRequest request) {
        createGroupChatPortIn.execute(request);
    }

    @GetMapping
    public List<ChatResponse> findAllUserChats(JwtAuthenticationToken token) {
        var userId = String.valueOf(token.getTokenAttributes().get("sub"));
        return listUserChatPortIn.execute(UUID.fromString(userId));
    }

    @GetMapping("/{chatId}")
    public ChatResponse findChatById(@PathVariable UUID chatId) {
        return listChatPortIn.execute(chatId);
    }

    @PostMapping("/{senderId}")
    @ResponseStatus(HttpStatus.CREATED)
    public UUID startPrivateChat(@PathVariable UUID senderId, @RequestBody @Valid CreatePrivateChatRequest request) {
        request.setSenderId(senderId);
        return createPrivateChatPortIn.execute(request);
    }
}
