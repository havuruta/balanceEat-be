CREATE TABLE challenge_ranking (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    score INT NOT NULL,
    ranking INT NOT NULL,
    recorded_date DATE NOT NULL, -- 예: 2025-05-20

    CONSTRAINT fk_challenge_ranking_user FOREIGN KEY (user_id) REFERENCES users(id),
    UNIQUE KEY uq_user_date (user_id, recorded_date) -- 한 유저가 같은 주에 중복 기록되지 않도록
);
