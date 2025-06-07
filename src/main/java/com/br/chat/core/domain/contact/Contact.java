package com.br.chat.core.domain.contact;

import com.br.chat.core.domain.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
@NoArgsConstructor
public class Contact {

    private Long id;
    private User requesterUser;
    private User requestedUser;
    private ContactRequestStatus status;
    private ZonedDateTime updatedAt;


    public Contact(User requesterUser, User requestedUser, ContactRequestStatus status, ZonedDateTime updatedAt) {
        this.requesterUser = requesterUser;
        this.requestedUser = requestedUser;
        this.status = status;
        this.updatedAt = updatedAt;
    }

    public Boolean isAccepted() {
        return this.status.equals(ContactRequestStatus.ACCEPTED);
    }

    public Boolean isPending() {
        return this.status.equals(ContactRequestStatus.PENDING);
    }

}
