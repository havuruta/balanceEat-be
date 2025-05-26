package com.balanceeat.demo.domain.ai.config;

import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepositoryDialect;
import org.springframework.ai.chat.memory.repository.jdbc.MysqlChatMemoryRepositoryDialect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatMemoryDialectFix {
	
	/** 창 크기를 10으로 고정(Inline) */
	private static final int WINDOW_SIZE = 10;
	
	@Bean
	JdbcChatMemoryRepositoryDialect mysqlChatDialect() {
		return new MysqlChatMemoryRepositoryDialect() {
			
			/** LIMIT 파라미터 문제를 없애기 위해 숫자를 인라인 */
			@Override
			public String getSelectMessagesSql() {
				return "SELECT content, type " +
					"FROM SPRING_AI_CHAT_MEMORY " +
					"WHERE conversation_id = ? " +
					"ORDER BY `timestamp` DESC " +
					"LIMIT " + WINDOW_SIZE;
			}
		};
	}
}
