create table diets
(
    id            bigint auto_increment
        primary key,
    user_id       bigint       not null,
    nutrition_id  bigint       not null,
    food_name     VARCHAR(100) not null,
    diet_date     date         not null,
    meal_type     varchar(20)  not null,
    meal_time     time         not null,
    amount        int          not null,
    note          text         null
);

create index idx_user_date
    on diets (user_id, diet_date);

create index idx_nutrition
    on diets (nutrition_id);