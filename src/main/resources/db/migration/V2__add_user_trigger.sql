DELIMITER $$

CREATE TRIGGER after_user_insert
AFTER INSERT ON users
FOR EACH ROW
BEGIN
    INSERT INTO friendship (user1_id, user2_id, status)
    SELECT user_id, NEW.user_id, 'SENT'
    FROM users
    WHERE user_id != NEW.user_id;
END$$

DELIMITER ;
