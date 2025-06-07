package com.br.chat.adapter.out.persistence.contact;

import com.br.chat.adapter.out.persistence.user.UserEntity;
import com.br.chat.core.domain.contact.Contact;
import com.br.chat.core.domain.contact.ContactRequestStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
@Entity
@Table(name = "contacts")
public class ContactEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "requester_id")
    private UserEntity requesterUser;

    @ManyToOne
    @JoinColumn(name = "requested_id")
    private UserEntity requestedUser;

    private String status;

    private ZonedDateTime updatedAt = ZonedDateTime.now();

    public static ContactEntity fromDomain(Contact contact) {
        var entity = new ContactEntity();
        entity.setId(contact.getId());
        entity.setRequesterUser(UserEntity.fromDomain(contact.getRequesterUser()));
        entity.setRequestedUser(UserEntity.fromDomain(contact.getRequestedUser()));
        entity.setStatus(contact.getStatus().name());
        entity.setUpdatedAt(contact.getUpdatedAt());
        return entity;
    }

    public Contact toDomain() {
        var contactRequest = new Contact();
        contactRequest.setId(this.id);
        contactRequest.setRequesterUser(this.requesterUser.toDomain());
        contactRequest.setRequestedUser(this.requestedUser.toDomain());
        contactRequest.setStatus(ContactRequestStatus.valueOf(this.status));
        contactRequest.setUpdatedAt(this.updatedAt);
        return contactRequest;
    }
}
