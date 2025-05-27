package com.balanceeat.demo.domain.chat.mapper;

import com.balanceeat.demo.domain.chat.entity.ChatMessage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface ChatMessageMapper {
    void insert(ChatMessage message);
    List<ChatMessage> findBySessionIdOrderByCreatedAtAsc(@Param("sessionId") String sessionId);
    void deleteBySessionId(@Param("sessionId") String sessionId);
} 