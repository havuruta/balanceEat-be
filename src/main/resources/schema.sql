CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS diets (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    diet_date DATE NOT NULL,
    meal_type VARCHAR(20) NOT NULL, -- BREAKFAST, LUNCH, DINNER
    food_name VARCHAR(100) NOT NULL,
    amount DOUBLE NOT NULL, -- g 단위
    calories DOUBLE NOT NULL,
    protein DOUBLE NOT NULL,
    fat DOUBLE NOT NULL,
    carbohydrates DOUBLE NOT NULL,
    note TEXT,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id),
    INDEX idx_user_date (user_id, diet_date)
);

CREATE TABLE IF NOT EXISTS diet_summaries (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    summary_date DATE NOT NULL,
    
    -- 아침 식사 영양 정보
    breakfast_calories DOUBLE NOT NULL DEFAULT 0,
    breakfast_protein DOUBLE NOT NULL DEFAULT 0,
    breakfast_fat DOUBLE NOT NULL DEFAULT 0,
    breakfast_carbohydrates DOUBLE NOT NULL DEFAULT 0,
    
    -- 점심 식사 영양 정보
    lunch_calories DOUBLE NOT NULL DEFAULT 0,
    lunch_protein DOUBLE NOT NULL DEFAULT 0,
    lunch_fat DOUBLE NOT NULL DEFAULT 0,
    lunch_carbohydrates DOUBLE NOT NULL DEFAULT 0,
    
    -- 저녁 식사 영양 정보
    dinner_calories DOUBLE NOT NULL DEFAULT 0,
    dinner_protein DOUBLE NOT NULL DEFAULT 0,
    dinner_fat DOUBLE NOT NULL DEFAULT 0,
    dinner_carbohydrates DOUBLE NOT NULL DEFAULT 0,
    
    -- 일일 총 영양 정보
    total_calories DOUBLE NOT NULL DEFAULT 0,
    total_protein DOUBLE NOT NULL DEFAULT 0,
    total_fat DOUBLE NOT NULL DEFAULT 0,
    total_carbohydrates DOUBLE NOT NULL DEFAULT 0,
    
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id),
    UNIQUE KEY uk_user_date (user_id, summary_date)
); 