package com.balanceeat.demo.domain.chat.repository;

import com.balanceeat.demo.domain.chat.entity.ChatSession;
import org.springframework.data.repository.CrudRepository;

public interface ChatSessionRepository extends CrudRepository<ChatSession, String> {
} 