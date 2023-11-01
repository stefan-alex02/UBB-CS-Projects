INSERT INTO users(first_name, last_name) VALUES
	('Ion', 'Popescu'),
	('Cosmin', 'Andrei'),
	('Dan', 'Danescu'),
	('Pop', 'Ionescu');
	
SELECT *
FROM users;


INSERT INTO friendships(id1, id2) VALUES
	(1, 2),
	(1, 3);
	
SELECT *
FROM friendships;
