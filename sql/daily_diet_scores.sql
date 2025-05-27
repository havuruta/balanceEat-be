CREATE TABLE IF NOT EXISTS diet_score_result (
                                                 user_id BIGINT NOT NULL,
                                                 date DATE NOT NULL,
                                                 score INT NOT NULL,
                                                 feedback VARCHAR(255) NOT NULL,
                                                 calorieAnalysis VARCHAR(255) NOT NULL,
                                                 nutrientAnalysis VARCHAR(255) NOT NULL,
                                                 suggestions1 VARCHAR(255) NOT NULL,
                                                 suggestions2 VARCHAR(255) NOT NULL,
                                                 suggestions3 VARCHAR(255) NOT NULL,
                                                 created_at TIMESTAMP NOT NULL,
                                                 PRIMARY KEY (user_id, date),
                                                 FOREIGN KEY (user_id) REFERENCES users(id)
);