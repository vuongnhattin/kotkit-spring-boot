CREATE TABLE users
(
    user_id           INT AUTO_INCREMENT PRIMARY KEY,
    email             VARCHAR(255),
    full_name         VARCHAR(255),
    password          VARCHAR(255),
    avatar            text,
    birthday          DATE,
    gender            ENUM ('MALE', 'FEMALE', 'OTHER'),
    number_of_friends INT              DEFAULT 0,
    number_of_videos  INT              DEFAULT 0,
    is_blocked        TINYINT NOT NULL DEFAULT 0,
    is_verified       TINYINT NOT NULL DEFAULT 0,
    roles             VARCHAR(255),
    created_at        TIMESTAMP        DEFAULT CURRENT_TIMESTAMP,
    updated_at        TIMESTAMP        DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE friendship
(
    friendship_id INT AUTO_INCREMENT PRIMARY KEY,
    user1_id      INT,
    user2_id      INT,
    status        ENUM ('SENT', 'RECEIVED', 'FRIEND'),
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user1_id) REFERENCES users (user_id) ON DELETE SET NULL,
    FOREIGN KEY (user2_id) REFERENCES users (user_id) ON DELETE SET NULL,
    UNIQUE (user1_id, user2_id)
);

CREATE TABLE video
(
    video_id           INT AUTO_INCREMENT PRIMARY KEY,
    creator_id         INT,
    title              VARCHAR(255),
    video_url          TEXT,
    thumbnail          TEXT,
    number_of_views    INT       DEFAULT 0,
    number_of_likes    INT       DEFAULT 0,
    number_of_comments INT       DEFAULT 0,
    mode               ENUM ('PUBLIC', 'PRIVATE'),
    created_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (creator_id) REFERENCES users (user_id) ON DELETE SET NULL
);

CREATE TABLE saved_video
(
    saved_video_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id        INT,
    video_id       INT,
    created_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE SET NULL,
    FOREIGN KEY (video_id) REFERENCES video (video_id) ON DELETE SET NULL
);

CREATE TABLE comment
(
    comment_id INT AUTO_INCREMENT PRIMARY KEY,
    author_id  INT,
    video_id   INT,
    comment    TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (author_id) REFERENCES users (user_id) ON DELETE SET NULL,
    FOREIGN KEY (video_id) REFERENCES video (video_id) ON DELETE SET NULL
);

CREATE TABLE `like`
(
    like_id    INT AUTO_INCREMENT PRIMARY KEY,
    user_id    INT,
    video_id   INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE SET NULL,
    FOREIGN KEY (video_id) REFERENCES video (video_id) ON DELETE SET NULL
);

CREATE TABLE notification
(
    notification_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id         INT,
    message         TEXT,
    type            ENUM ('VIDEO', 'FRIEND'),
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE SET NULL
);

CREATE TABLE room
(
    room_id    INT AUTO_INCREMENT PRIMARY KEY,
    room_name  VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE user_room
(
    user_room_id INT AUTO_INCREMENT PRIMARY KEY,
    room_id      INT,
    user_id      INT,
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (room_id) REFERENCES room (room_id) ON DELETE SET NULL,
    FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE SET NULL
);

CREATE TABLE message
(
    message_id INT AUTO_INCREMENT PRIMARY KEY,
    room_id    INT,
    sender_id  INT,
    message    TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (room_id) REFERENCES room (room_id) ON DELETE SET NULL,
    FOREIGN KEY (sender_id) REFERENCES users (user_id) ON DELETE SET NULL
);
