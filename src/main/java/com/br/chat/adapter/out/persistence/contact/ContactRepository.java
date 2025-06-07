package com.br.chat.adapter.out.persistence.contact;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ContactRepository extends JpaRepository<ContactEntity, Long> {

    List<ContactEntity> findAllByRequestedUserId(UUID userId);

    @Query(value = """
            SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END
            FROM ContactEntity c
            WHERE (c.status = 'PENDING' OR c.status = 'ACCEPTED')
            AND ((c.requestedUser.id = :requestedId AND c.requesterUser.id = :requesterId)
            OR  (c.requesterUser.id = :requestedId AND c.requestedUser.id = :requesterId))
            """)
    Boolean existsActiveRequestsByRequesterIdAndRequestedId(UUID requesterId, UUID requestedId);

    @Query(value = """
            SELECT c FROM ContactEntity c
            WHERE c.requestedUser.id = :userId OR c.requesterUser.id = :userId
            """)
    List<ContactEntity> findAllByUserId(UUID userId);

}
