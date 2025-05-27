-- 사용자 테이블
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    nickname VARCHAR(50) NOT NULL,
    profile_image VARCHAR(255),
    height DECIMAL(5,2),
    weight DECIMAL(5,2),
    birth_date DATE,
    gender VARCHAR(10),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    is_deleted BOOLEAN DEFAULT FALSE
);


CREATE TABLE IF NOT EXISTS diet_summaries (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    summary_date DATE NOT NULL,
    
    -- 아침 식사 영양 정보
    breakfast_calories DOUBLE DEFAULT 0,
    
    -- 점심 식사 영양 정보
    lunch_calories DOUBLE DEFAULT 0,
    
    -- 저녁 식사 영양 정보
    dinner_calories DOUBLE DEFAULT 0,
    
    -- 간식 영양 정보
    snack_calories DOUBLE DEFAULT 0,
    
    -- 야식 영양 정보
    night_calories DOUBLE DEFAULT 0,
    
    -- 일일 총 영양 정보
    total_calories DOUBLE DEFAULT 0,
    
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (user_id) REFERENCES users(id),
    UNIQUE KEY unique_user_date (user_id, summary_date)
);

CREATE TABLE IF NOT EXISTS diets (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    nutrition_id BIGINT NOT NULL,
    food_name VARCHAR(100) NOT NULL,
    amount INTEGER NOT NULL,
    note VARCHAR(255),
    meal_type VARCHAR(20) NOT NULL,
    diet_date DATE NOT NULL,
    meal_time TIME NOT NULL
);

CREATE TABLE IF NOT EXISTS diet_score_result (
    user_id BIGINT NOT NULL,
    date DATE NOT NULL,
    score INT NOT NULL,
    feedback VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    PRIMARY KEY (user_id, date),
    FOREIGN KEY (user_id) REFERENCES users(id)


    -- 챌린지 랭킹 테이블
CREATE TABLE IF NOT EXISTS challenge_ranking (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    challenge_id BIGINT NOT NULL,
    score INTEGER NOT NULL,
    rank INTEGER NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- 채팅 세션 테이블
CREATE TABLE IF NOT EXISTS chat_sessions (
    session_id VARCHAR(36) PRIMARY KEY,
    user_id BIGINT NOT NULL,
    title VARCHAR(100) NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- 채팅 메시지 테이블
CREATE TABLE IF NOT EXISTS chat_messages (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    session_id VARCHAR(36) NOT NULL,
    role VARCHAR(10) NOT NULL,
    content TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    FOREIGN KEY (session_id) REFERENCES chat_sessions(session_id)
);

-- 인덱스 생성
CREATE INDEX idx_diets_user_date ON diets(user_id, diet_date);
CREATE INDEX idx_diet_summaries_user_date ON diet_summaries(user_id, summary_date);
CREATE INDEX idx_chat_sessions_user_id ON chat_sessions(user_id);
CREATE INDEX idx_chat_messages_session_id ON chat_messages(session_id);
CREATE INDEX idx_challenge_ranking_user_challenge ON challenge_ranking(user_id, challenge_id);
);
