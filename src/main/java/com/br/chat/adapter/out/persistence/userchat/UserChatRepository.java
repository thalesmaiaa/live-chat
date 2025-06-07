package com.br.chat.adapter.out.persistence.userchat;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserChatRepository extends JpaRepository<UserChatEntity, UserChatId> {
}
