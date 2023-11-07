INSERT INTO users(first_name, last_name) VALUES
	('firstName1', 'lastName1'),
	('firstName2', 'lastName2'),
	('firstName3', 'lastName3'),
	('firstName4', 'lastName4');
	
SELECT *
FROM users;


INSERT INTO friendships(id1, id2) VALUES
	(1, 2),
	(1, 3),
	(3, 4);
	
SELECT *
FROM friendships;

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
