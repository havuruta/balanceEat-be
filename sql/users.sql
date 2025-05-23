SHOW DATABASES ;

USE balance_eat;

CREATE TABLE users (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,

    -- 유저 정보
                       nickname VARCHAR(50) NOT NULL,
                       birth_year INT NOT NULL,
                       gender ENUM('MALE', 'FEMALE', 'OTHER') NOT NULL,
                       weight INT NOT NULL,
                       height INT NOT NULL,
                       disease_code TEXT,
                       diet_habit TEXT,
                       food_blacklist TEXT,
                       food_preference TEXT,

    -- 감사 로그
                       created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                       updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    -- 회원 탈퇴 여부
                       is_active BOOLEAN DEFAULT TRUE
);

