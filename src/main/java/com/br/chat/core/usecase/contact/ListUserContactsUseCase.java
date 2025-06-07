package com.br.chat.core.usecase.contact;

import com.br.chat.adapter.in.dto.responses.ContactResponse;
import com.br.chat.core.domain.chat.Chat;
import com.br.chat.core.domain.chat.PrivateChat;
import com.br.chat.core.domain.contact.Contact;
import com.br.chat.core.domain.contact.ContactUserReference;
import com.br.chat.core.port.in.contact.ListUserContactsPortIn;
import com.br.chat.core.port.out.ChatRepositoryPortOut;
import com.br.chat.core.port.out.ContactRepositoryPortOut;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ListUserContactsUseCase implements ListUserContactsPortIn {

    private final ContactRepositoryPortOut contactRepositoryPortOut;
    private final ChatRepositoryPortOut chatRepositoryPortOut;

    @Override
    public List<ContactResponse> execute(UUID userId) {
        var contacts = contactRepositoryPortOut.findAllAcceptedContactsByUserId(userId);
        if (contacts.isEmpty()) return List.of();

        var chats = chatRepositoryPortOut.findAllByUserId(userId);
        var userContactReferences = getUserContactReferences(userId, contacts);
        var privateChats = extractUserPrivateChats(userId, chats);

        updateContactReferencesWithChatInfo(userContactReferences, privateChats);

        return userContactReferences.stream()
                .map(ContactUserReference::toResponse)
                .toList();
    }

    private void updateContactReferencesWithChatInfo(List<ContactUserReference> userContactReferences, List<PrivateChat> privateChats) {
        var privateChatUserIds = privateChats.stream().map(pc -> pc.getUser().getId()).toList();
        userContactReferences.forEach(contact -> {
            var contactUserId = contact.getUser().getId();
            var contactHasActiveChat = privateChatUserIds.contains(contactUserId);
            var privateChat = privateChats.stream().filter(chat -> chat.getUser().getId().equals(contactUserId))
                    .findFirst();
            contact.setHasActiveChat(contactHasActiveChat);
            contact.setChatId(privateChat.map(PrivateChat::getId).orElse(null));
        });
    }

    private List<ContactUserReference> getUserContactReferences(UUID userId, List<Contact> contacts) {
        return contacts.stream().map(contact -> {
            var isUserContactSender = contact.getRequesterUser().getId().equals(userId);
            var user = isUserContactSender ? contact.getRequestedUser() : contact.getRequesterUser();
            return new ContactUserReference(contact.getId(), user);
        }).toList();
    }

    private List<PrivateChat> extractUserPrivateChats(UUID userId, List<Chat> chats) {
        return chats.stream()
                .filter(Chat::isPrivateChat)
                .map(chat -> {
                    var otherUser = chat.getUsers().stream()
                            .filter(u -> !u.getId().equals(userId))
                            .findFirst();
                    return new PrivateChat(chat.getId(), otherUser.orElse(null));
                }).toList();
    }
}