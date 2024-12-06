create table users (
    id int primary key auto_increment,
    username varchar(255),
    fullName varchar(255),
    password varchar(255),
    roles varchar(255),
    avatar varchar(255),
    created_at timestamp default current_timestamp,
    updated_at timestamp default current_timestamp
)