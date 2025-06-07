package com.br.chat.core.usecase.contact;

import com.br.chat.adapter.in.dto.responses.PendingContactResponse;
import com.br.chat.core.domain.contact.Contact;
import com.br.chat.core.domain.user.User;
import com.br.chat.core.port.in.contact.ListUserPendingContactsPortIn;
import com.br.chat.core.port.out.ContactRepositoryPortOut;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ListUserPendingContactsUseCase implements ListUserPendingContactsPortIn {

    private final ContactRepositoryPortOut contactRepositoryPortOut;

    @Override
    public List<PendingContactResponse> execute(UUID userId) {
        var contactRequests = contactRepositoryPortOut.findAllByRequestedId(userId);
        return contactRequests.stream().filter(Contact::isPending)
                .filter(c -> c.getRequestedUser().getId().equals(userId))
                .map(c ->
                        new PendingContactResponse(c.getId(), User.toUserResponse(c.getRequesterUser()))).toList();
    }
}