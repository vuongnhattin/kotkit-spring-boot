create table friendship (
    id int primary key auto_increment,
    sender_id int,
    receiver_id int,
    status varchar(255),
    created_at timestamp default current_timestamp,
    updated_at timestamp default current_timestamp,
    foreign key (sender_id) references users(id) on delete cascade,
    foreign key (receiver_id) references users(id) on delete cascade
)