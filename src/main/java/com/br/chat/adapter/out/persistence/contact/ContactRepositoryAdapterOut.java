package com.br.chat.adapter.out.persistence.contact;

import com.br.chat.core.domain.contact.Contact;
import com.br.chat.core.port.out.ContactRepositoryPortOut;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ContactRepositoryAdapterOut implements ContactRepositoryPortOut {

    private final ContactRepository contactRepository;

    @Override
    public List<Contact> findAllByRequestedId(UUID userId) {
        return contactRepository.findAllByRequestedUserId(userId).stream().map(ContactEntity::toDomain).toList();
    }

    @Override
    public void save(Contact request) {
        var contactRequestEntity = ContactEntity.fromDomain(request);
        contactRepository.save(contactRequestEntity);
    }

    @Override
    public Optional<Contact> findById(Long id) {
        return contactRepository.findById(id).map(ContactEntity::toDomain);
    }

    @Override
    public Boolean existsActiveRequestsByRequesterIdAndRequestedId(UUID requesterId, UUID requestedId) {
        return contactRepository.existsActiveRequestsByRequesterIdAndRequestedId(requesterId, requestedId);
    }

    @Override
    public List<Contact> findAllAcceptedContactsByUserId(UUID userId) {
        return contactRepository.findAllByUserId(userId).stream().map(ContactEntity::toDomain)
                .filter(Contact::isAccepted).toList();
    }

    @Override
    public void deleteById(Long id) {
        contactRepository.deleteById(id);
    }

}
