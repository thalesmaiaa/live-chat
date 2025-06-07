package com.br.chat.adapter.out.persistence.chat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ChatRepository extends JpaRepository<ChatEntity, UUID> {

    @Query(value = """
            SELECT c FROM ChatEntity c
            INNER JOIN c.users u
            WHERE u.user.id = :userId
            """)
    List<ChatEntity> findAllByUserId(UUID userId);
}
