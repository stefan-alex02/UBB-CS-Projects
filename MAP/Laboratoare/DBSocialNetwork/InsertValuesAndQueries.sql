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
