-- Insert into users
INSERT INTO users (email, full_name, password, avatar, birthday, gender, number_of_friends, number_of_videos, is_blocked, is_verified, roles)
VALUES
('user1@example.com', 'John Doe', 'password123', 'avatar1.png', '1990-01-01', 'MALE', 3, 2, 0, 1, 'USER'),
('user2@example.com', 'Jane Smith', 'password456', 'avatar2.png', '1992-02-02', 'FEMALE', 2, 1, 0, 1, 'USER'),
('user3@example.com', 'Sam Taylor', 'password789', 'avatar3.png', '1994-03-03', 'OTHER', 1, 1, 0, 0, 'ADMIN'),
('user4@example.com', 'Alice Johnson', 'passwordabc', 'avatar4.png', '1988-04-04', 'FEMALE', 1, 0, 0, 1, 'USER'),
('user5@example.com', 'Bob Brown', 'passworddef', 'avatar5.png', '1996-05-05', 'MALE', 0, 0, 0, 0, 'USER');

-- Insert into friendship
INSERT INTO friendship (user1_id, user2_id, status)
VALUES
(1, 2, 'FRIEND'),
(2, 3, 'SENT'),
(3, 4, 'RECEIVED'),
(4, 5, 'FRIEND'),
(1, 3, 'FRIEND');

-- Insert into video
INSERT INTO video (creator_id, title, video_url, thumbnail, number_of_views, number_of_likes, number_of_comments, mode)
VALUES
(1, 'First Video', 'http://video1.com', 'thumb1.png', 100, 10, 5, 'PUBLIC'),
(2, 'Second Video', 'http://video2.com', 'thumb2.png', 200, 20, 10, 'PRIVATE'),
(3, 'Third Video', 'http://video3.com', 'thumb3.png', 300, 30, 15, 'PUBLIC'),
(1, 'Fourth Video', 'http://video4.com', 'thumb4.png', 150, 15, 7, 'PRIVATE'),
(2, 'Fifth Video', 'http://video5.com', 'thumb5.png', 250, 25, 12, 'PUBLIC');

-- Insert into saved_video
INSERT INTO saved_video (user_id, video_id)
VALUES
(1, 1),
(2, 2),
(3, 3),
(4, 4),
(5, 5);

-- Insert into comment
INSERT INTO comment (author_id, video_id, comment)
VALUES
(1, 1, 'Great video!'),
(2, 1, 'I love this content.'),
(3, 2, 'Very informative.'),
(4, 3, 'Nice editing!'),
(5, 4, 'Cool stuff.');

-- Insert into `like`
INSERT INTO `like` (user_id, video_id)
VALUES
(1, 1),
(2, 2),
(3, 3),
(4, 4),
(5, 5);

-- Insert into notification
INSERT INTO notification (user_id, message, type)
VALUES
(1, 'Your friend request has been accepted.', 'FRIEND'),
(2, 'Your video has a new like!', 'VIDEO'),
(3, 'You have a new comment on your video.', 'VIDEO'),
(4, 'Friend request sent successfully.', 'FRIEND'),
(5, 'Your account is now verified.', 'FRIEND');

-- Insert into room
INSERT INTO room (room_name)
VALUES
('Room 1'),
('Room 2'),
('Room 3'),
('Room 4'),
('Room 5');

-- Insert into user_room
INSERT INTO user_room (room_id, user_id)
VALUES
(1, 1),
(2, 2),
(3, 3),
(4, 4),
(5, 5);

-- Insert into message
INSERT INTO message (room_id, sender_id, message)
VALUES
(1, 1, 'Hello, everyone!'),
(2, 2, 'Good morning!'),
(3, 3, 'What’s up?'),
(4, 4, 'Hi there!'),
(5, 5, 'Let’s chat.');
