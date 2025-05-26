CREATE TABLE daily_diet_scores (
                                   user_id    BIGINT NOT NULL,
                                   date       DATE   NOT NULL,
                                   score      INT    NOT NULL,
                                   feedback   VARCHAR(255),
                                   created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                   PRIMARY KEY (user_id, date)
);
