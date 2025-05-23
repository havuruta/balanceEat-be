create table diet_summaries
(
    id                      bigint auto_increment
        primary key,
    user_id                 bigint           not null,
    summary_date            date             not null,
    breakfast_calories      double default 0 not null,
    breakfast_protein       double default 0 not null,
    breakfast_fat           double default 0 not null,
    breakfast_carbohydrates double default 0 not null,
    lunch_calories          double default 0 not null,
    lunch_protein           double default 0 not null,
    lunch_fat               double default 0 not null,
    lunch_carbohydrates     double default 0 not null,
    dinner_calories         double default 0 not null,
    dinner_protein          double default 0 not null,
    dinner_fat              double default 0 not null,
    dinner_carbohydrates    double default 0 not null,
    total_calories          double default 0 not null,
    total_protein           double default 0 not null,
    total_fat               double default 0 not null,
    total_carbohydrates     double default 0 not null,
    created_at              timestamp        not null,
    updated_at              timestamp        not null,
    constraint uk_user_date
        unique (user_id, summary_date),
    constraint diet_summaries_ibfk_1
        foreign key (user_id) references users (id)
);

