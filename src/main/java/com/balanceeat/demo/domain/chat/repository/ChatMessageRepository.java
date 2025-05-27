package com.balanceeat.demo.domain.chat.repository;

import com.balanceeat.demo.domain.chat.entity.ChatMessage;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface ChatMessageRepository extends CrudRepository<ChatMessage, Long> {
    List<ChatMessage> findBySessionIdOrderByCreatedAtAsc(String sessionId);
} 