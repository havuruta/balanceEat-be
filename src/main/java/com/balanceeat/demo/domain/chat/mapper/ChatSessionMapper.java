package com.balanceeat.demo.domain.chat.mapper;

import com.balanceeat.demo.domain.chat.entity.ChatSession;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.Optional;

@Mapper
public interface ChatSessionMapper {
    void insert(ChatSession session);
    Optional<ChatSession> findById(@Param("sessionId") String sessionId);
    void update(ChatSession session);
    void delete(@Param("sessionId") String sessionId);
} 