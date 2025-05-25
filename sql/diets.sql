create table diets
(
    id            bigint auto_increment
        primary key,
    user_id       bigint       not null,
    diet_date     date         not null,
    meal_type     varchar(20)  not null,
    food_name     varchar(100) not null,
    amount        double       not null,
    note          text         null,
    created_at    timestamp    not null,
    updated_at    timestamp    not null,
    constraint diets_ibfk_1
        foreign key (user_id) references users (id)
);

create index idx_user_date
    on diets (user_id, diet_date);