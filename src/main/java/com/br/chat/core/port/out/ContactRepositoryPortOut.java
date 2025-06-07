package com.br.chat.core.port.out;

import com.br.chat.core.domain.contact.Contact;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ContactRepositoryPortOut {


    void save(Contact contact);

    List<Contact> findAllByRequestedId(UUID userId);

    Optional<Contact> findById(Long uuid);

    Boolean existsActiveRequestsByRequesterIdAndRequestedId(UUID requesterId, UUID requestedId);

    List<Contact> findAllAcceptedContactsByUserId(UUID userId);

    void deleteById(Long id);
}