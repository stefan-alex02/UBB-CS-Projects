INSERT INTO users(first_name, last_name) VALUES
	('firstName1', 'lastName1'),
	('firstName2', 'lastName2'),
	('firstName3', 'lastName3'),
	('firstName4', 'lastName4');

INSERT INTO friendships(id1, id2) VALUES
	(1, 2),
	(1, 3),
	(3, 4);

SELECT F.id1, 
U1.first_name AS first_name1,
U1.last_name AS last_name1,
F.id2, 
U2.first_name AS first_name2,
U2.last_name AS last_name2,
F.friends_from
FROM friendships F
INNER JOIN users U1 ON U1.id = F.id1
INNER JOIN users U2 ON U2.id = F.id2
WHERE F.id1 = 3 AND F.id2 = 4

SELECT F.id2 AS id, 
U.first_name AS first_name,
U.last_name AS last_name
FROM friendships F
INNER JOIN users U ON U.id = F.id2
WHERE F.id1 = 2
UNION
SELECT F.id1 AS id, 
U.first_name AS first_name,
U.last_name AS last_name
FROM friendships F
INNER JOIN users U ON U.id = F.id1
WHERE F.id2 = 2
ORDER BY id;


UPDATE friendships
SET id2 = 3
WHERE id1 = 1 AND id2 = 2;



SELECT *
FROM users;

SELECT *
FROM friendships;
