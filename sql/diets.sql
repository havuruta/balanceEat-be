create table diets
(
    id            bigint auto_increment
        primary key,
    user_id       bigint       not null,
    diet_date     date         not null,
    meal_type     varchar(20)  not null,
    food_name     varchar(100) not null,
    amount        double       not null,
    calories      double       not null,
    protein       double       not null,
    fat           double       not null,
    carbohydrates double       not null,
    note          text         null,
    created_at    timestamp    not null,
    updated_at    timestamp    not null,
    constraint diets_ibfk_1
        foreign key (user_id) references users (id)
);

create index idx_user_date
    on diets (user_id, diet_date);

INSERT INTO balanceeat.diets (id, user_id, diet_date, meal_type, food_name, amount, calories, protein, fat, carbohydrates, note, created_at, updated_at) VALUES (2, 1, '2025-04-26', 'BREAKFAST', '국밥_돼지머리', 50, 68.5, 3.35, 2.58, 7.97, '', '2025-04-26 02:55:32', '2025-04-26 02:55:32');
INSERT INTO balanceeat.diets (id, user_id, diet_date, meal_type, food_name, amount, calories, protein, fat, carbohydrates, note, created_at, updated_at) VALUES (4, 1, '2025-04-26', 'BREAKFAST', '기장밥', 1, 1.6600000000000001, 0.0344, 0.005699999999999999, 0.3677, '', '2025-04-26 03:02:41', '2025-04-26 03:02:41');
INSERT INTO balanceeat.diets (id, user_id, diet_date, meal_type, food_name, amount, calories, protein, fat, carbohydrates, note, created_at, updated_at) VALUES (6, 1, '2025-04-25', 'LUNCH', '김밥', 200, 280, 9.68, 9.1, 39.96, '', '2025-04-26 03:51:24', '2025-04-26 03:51:24');
INSERT INTO balanceeat.diets (id, user_id, diet_date, meal_type, food_name, amount, calories, protein, fat, carbohydrates, note, created_at, updated_at) VALUES (7, 1, '2025-04-27', 'DINNER', '현미밥', 100, 172, 3.1, 0.47, 38.9, '', '2025-04-26 16:14:52', '2025-04-26 16:14:52');
INSERT INTO balanceeat.diets (id, user_id, diet_date, meal_type, food_name, amount, calories, protein, fat, carbohydrates, note, created_at, updated_at) VALUES (8, 1, '2025-04-26', 'LUNCH', '국밥_순대국밥', 188, 141, 5.959599999999999, 4.2863999999999995, 19.514400000000002, '', '2025-04-26 16:42:15', '2025-04-26 16:42:15');
ㅋ