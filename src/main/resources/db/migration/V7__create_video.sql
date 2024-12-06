create table video (
    id int primary key auto_increment,
    title varchar(255),
    video varchar(255),
    thumbnail varchar(255),
    number_of_likes int,
    number_of_comments int,
    number_of_views int,
    creator_id int,
    visibility enum('PUBLIC', 'PRIVATE') default 'PUBLIC',

    created_at timestamp default current_timestamp,
    updated_at timestamp default current_timestamp,

    foreign key (creator_id) references users(id) on delete cascade
)