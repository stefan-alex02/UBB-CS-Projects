INSERT INTO users(first_name, last_name) VALUES
	('firstName1', 'lastName1'),
	('firstName2', 'lastName2'),
	('firstName3', 'lastName3'),
	('firstName4', 'lastName4');

INSERT INTO friendships(id1, id2, friends_from) VALUES
	(1, 2, '20230101'),
	(1, 3, '20230101'),
	(3, 4, '20230101');

-- SELECT F.id1, 
-- U1.first_name AS first_name1,
-- U1.last_name AS last_name1,
-- F.id2, 
-- U2.first_name AS first_name2,
-- U2.last_name AS last_name2,
-- F.friends_from
-- FROM friendships F
-- INNER JOIN users U1 ON U1.id = F.id1
-- INNER JOIN users U2 ON U2.id = F.id2
-- WHERE F.id1 = 3 AND F.id2 = 4

-- SELECT F.id2 AS id, 
-- U.first_name AS first_name,
-- U.last_name AS last_name
-- FROM friendships F
-- INNER JOIN users U ON U.id = F.id2
-- WHERE F.id1 = 2
-- UNION
-- SELECT F.id1 AS id, 
-- U.first_name AS first_name,
-- U.last_name AS last_name
-- FROM friendships F
-- INNER JOIN users U ON U.id = F.id1
-- WHERE F.id2 = 2
-- ORDER BY id;


-- UPDATE friendships
-- SET id2 = 3
-- WHERE id1 = 1 AND id2 = 2;

SELECT *
FROM users;

SELECT *
FROM friendships;


INSERT INTO messages(from_user_id, message, date) VALUES
	(3, 'Hello there', '2023-11-26 23:30'),
	(2, 'Hi!', '2023-11-26 23:31');
	
INSERT INTO messages_receivers(message_id, receiver_id) VALUES
	(1, 2),
	(1, 1),
	(1, 4),
	(2, 3),
	(2, 4);
	
INSERT INTO reply_messages(message_id, reply_to_id) VALUES
	(2, 1);

SELECT *
FROM messages;

SELECT *
FROM messages_receivers;

SELECT *
FROM reply_messages;

-- SELECT id, from_user_id, message, date, receiver_id, reply_to_id
-- FROM messages M
-- INNER JOIN messages_receivers R ON R.message_id = M.id
-- LEFT JOIN reply_messages RM ON RM.message_id = M.id;

SELECT M.id, M.from_user_id, U.first_name, U.last_name, M.message, M.date, 
	R.reply_to_id, M2.message as reply_to_message, 
	M2.from_user_id as reply_to_user_id, 
	U2.first_name as reply_to_user_first_name, 
	U2.last_name as reply_to_user_last_name,
	M2.date as reply_to_message_date
FROM messages M
INNER JOIN users U ON U.id = M.from_user_id
LEFT JOIN reply_messages R ON R.message_id = M.id
LEFT JOIN messages M2 ON M2.id = R.reply_to_id
LEFT JOIN users U2 ON U2.id = M2.from_user_id
WHERE M.id = 2;


SELECT M.id, M.from_user_id, U.id, U.first_name, U.last_name
FROM messages M
INNER JOIN messages_receivers MR ON MR.message_id = M.id
INNER JOIN users U ON U.id = MR.receiver_id
WHERE M.id = 1;


INSERT INTO friend_requests(from_id, to_id, date) VALUES
	(2, 3, '2023-11-26 12:45');
	
SELECT * FROM friend_requests;

