-- Adding records to all tables

USE Cinematography
GO

------------------------------------------------------------------------------------------
INSERT INTO Versions(versionId) VALUES
	(0)
------------------------------------------------------------------------------------------


------------------------------------------------------------------------------------------
INSERT INTO Genres(Name) VALUES
	('Sci-Fi'), ('Action'), ('Adventure'), ('Fantasy'), 
	('Romance'), ('Horror'), ('Drama'), ('Mistery'), 
	('Comedy'), ('Western'), ('Musical'), ('Fantasy'),
	('Animation')
------------------------------------------------------------------------------------------


------------------------------------------------------------------------------------------
INSERT INTO Directors(Name, DOB, Nationality, Contact) VALUES
	('Christopher Nolan', '19700730', 'English', 'IG: @christophernolann'),			-- 1
	('Kate Herron', NULL, 'English', NULL),											-- 2
	('Justin Benson', '19830609', 'American', NULL),								-- 3
	('John Lasseter', '19570112', 'American', NULL),								-- 4
	('Brian Fee', NULL, 'American', NULL),											-- 5
	('James Cameron', '19540816', 'Canadian', 'Twitter: @JimCameron'),				-- 6
	('Jonathan Mostow', '19611128', 'American', NULL),								-- 7
	('Alan Taylor', '19590113', 'American', NULL),									-- 8
	('Quentin Tarantino', '19630327', 'American', 'Twitter: @QTarantino_news'),		-- 9
	('The Wachowskis', NULL, 'American', NULL),										-- 10
	('Joss Whedon', '19640623', 'American', NULL),									-- 11
	('Hwang Dong-hyuk', '19710526', 'South Korean', NULL),							-- 12
	('Bryan Andrews', NULL, 'American', NULL),										-- 13
	('Chris Buck', '19580224', 'American', NULL)									-- 14
------------------------------------------------------------------------------------------


------------------------------------------------------------------------------------------
INSERT INTO Studios(Name, Location) VALUES
	('Paramount Pictures', 'California, United States'),						-- 1
	('Marvel Studios', 'California, United States'),							-- 2
	('Pixar Animation Studios', 'California, United States'),					-- 3
	('Warner Bros. Pictures', 'Burbank, California, United States'),			-- 4
	('TriStar Pictures', 'Burbank, California, United States'),					-- 5
	('Universal Pictures', 'Universal City, California, United States'),		-- 6
	('20th Century Studios', 'Century City, California, United States'),		-- 7
	('Sony Pictures', 'Culver City, California, United States'),				-- 8
	('DreamWorks Pictures', 'Universal City, California, United States'),		-- 9
	('Studio Lambert', 'London, United Kingdom'),								-- 10
	('Walt Disney Pictures', 'Burbank, California, United States')				-- 11
------------------------------------------------------------------------------------------


------------------------------------------------------------------------------------------
INSERT INTO Actors(Name, DOB, Nationality, Contact) VALUES
	('Matt Damon', '19701008', 'American', 'IG: @matt_damon_official'),				-- 1
	('Matthew McConaughey', '19691104', 'American', NULL),							-- 2
	('Anne Hathaway', '19821112', 'American', NULL),								-- 3
	('Michael Caine', '19330314', 'English', 'Twitter: @themichaelcaine'),			-- 4
	('Tom Hiddleston', '19810209', 'English', 'IG: @twhiddleston'),					-- 5
	('Mackenzie Anne Foy', '20001110', 'American', NULL),							-- 6
	('Sophia Di Martino', '19831115', 'English', 'IG: @itssophiadimartino'),		-- 7
	('Owen Wilson', '19681118', 'American', NULL),									-- 8
	('Jonathan Majors', '19890907', 'American', NULL),								-- 9
	('Gugu Mbatha-Raw', '19830421', 'English', NULL),								-- 10
	('Paul Newman', '19250126', 'American', NULL),									-- 11
	('Bonnie Lynn Hunt', '19610722', 'American', NULL),								-- 12
	('Larry the Cable Guy', '19630217', 'American', NULL),							-- 13
	('Emily Mortimer', '19711006', 'English', NULL),								-- 14
	('John Turturro', '19570228', 'American', NULL),								-- 15
	('Cristela Alonzo', NULL, 'American', NULL),									-- 16
	('Armie Hammer', '19860828', 'American', NULL),									-- 17
	('Leonardo DiCaprio', '19741111', 'American', 'IG: @leonardodicaprio'),			-- 18
	('Ken Watanabe', '19591021', 'Japanese', NULL),									-- 19
	('Joseph Gordon-Levitt', '19810217', 'American', NULL),							-- 20
	('Marion Cotillard', '19750930', 'French', NULL),								-- 21
	('Tom Hardy', '19770915', 'English', 'IG: @tomhardy'),							-- 22
	('Cillian Murphy', '19760525', 'Irish', 'IG: @cillianmurphyofficiall'),			-- 23
	('Kate Winslet', '19751005', 'English', NULL),									-- 24
	('Arnold Schwarzenegger', '19470730', 'Austrian', 'IG: @schwarzenegger'),		-- 25
	('Michael Biehn', '19560731', 'American', NULL),								-- 26
	('Linda Hamilton', '19560726', 'American', NULL),								-- 27
	('Edward Furlong', '19770802', 'American', NULL),								-- 28
	('Robert Patrick', '19581105', 'American', NULL),								-- 29
	('Brad Pitt', '19631218', 'American', 'Twitter: @RealBradPitt'),				-- 30
	('Margot Robbie', '19920702', 'Australian', 'IG: @margotrobbie'),				-- 31
	('Timothée Chalamet', '19931227', 'American', 'IG: @tchalamet'),				-- 32
	('Robert Downey Jr.', '19650404', 'American', 'IG: @robertdowneyjr'),			-- 33
	('Scarlett Johansson', '19841122', 'American', 'IG: @scarlettjohansson'),		-- 34
	('Chris Hemsworth', '19830811', 'Australian', 'IG: @chrishemsworth'),			-- 35
	('Keanu Reeves', '19640902', 'Canadian', NULL),									-- 36
	('Jason Clarke', '19690717', 'Australian', NULL),								-- 37
	('Emilia Clarke', '19861023', 'English', NULL),									-- 38
	('Jai Courtney', '19860314', 'Australian', NULL),								-- 39
	('J. K. Simmons', '19550109', 'American', NULL),								-- 40
	('Laurence Fishburne', '19610730', 'American', NULL),							-- 41
	('Carrie-Anne Moss', '19670821', 'Canadian', NULL),								-- 42
	('Hugo Weaving', '19600404', 'English', NULL),									-- 43
	('Chris Evans', '19810613', 'American', NULL),									-- 44
	('Mark Ruffalo', '19671122', 'American', 'IG: @markruffalo'),					-- 45
	('Jeremy Renner', '19710107', 'American', 'IG: @jeremyrenner'),					-- 46
	('Samuel L. Jackson', '19481221', 'American', 'IG: @samuelljackson'),			-- 47
	('Lee Jung-jae', '19721215', 'South Korean', NULL),								-- 48
	('Park Hae-soo', '19811121', 'South Korean', NULL),								-- 49
	('Wi Ha-joon', '19910805', 'South Korean', NULL),								-- 50
	('HoYeon Jung', '19940623', 'South Korean', NULL),								-- 51
	('O Yeong-su', '19440819', 'South Korean', NULL),								-- 52
	('Lee Byung-hun', '19700712', 'South Korean', NULL),							-- 53
	('Silviu Gherman', NULL, 'Romanian', 'YT: @SilviuGherman')						-- 54
------------------------------------------------------------------------------------------


------------------------------------------------------------------------------------------
INSERT INTO Movies(Title, DirectorID, LaunchDate, RunningTime) VALUES
	('Interstellar', 1, '20141107', 169),										-- 1
	('Cars', 4, '20060609', 117),												-- 2
	('Cars 2', 4, '20110624', 106),												-- 3
	('Cars 3', 5, '20170616', 102),												-- 4
	('Inception', 1, '20100730', 148)											-- 5
------------------------------------------------------------------------------------------


------------------------------------------------------------------------------------------
INSERT INTO MoviesGenres(MovieID, GenreID) VALUES
	(1, 1), (1, 3), (1, 7),													-- Interstellar
	(2, 13), (2, 3), (2, 9),												-- Cars 1
	(3, 13), (3, 3), (3, 9),												-- Cars 2
	(4, 13), (4, 3), (4, 9),												-- Cars 3
	(5, 1), (5, 2), (5, 3)													-- Inception
------------------------------------------------------------------------------------------


------------------------------------------------------------------------------------------
INSERT INTO MovieRoles(MovieID, ActorID, CharacterName, Role, FamousLine) VALUES
	(1, 1, 'Dr. Mann', 'Secondary', NULL),									-- Interstellar
	(1, 2, 'Joseph Cooper', 'Secondary', NULL),								-- Interstellar
	(1, 3, 'Dr. Amelia Brand', 'Lead', NULL),								-- Interstellar
	(1, 4, 'Professor John Brand', 'Secondary',								-- Interstellar
			'Do not go gentle into that good night,
			Old age should burn and rave at close of day;
			Rage, rage against the dying of the light.'),
	(1, 6, 'young Murph', 'Lead', NULL),									-- Interstellar
	(2, 8, 'Lightning McQueen', 'Lead', NULL),								-- Cars 1
	(2, 11, 'Doc Hudson', 'Secondary', NULL),								-- Cars 1
	(2, 12, 'Sally Carrera', 'Lead', NULL),									-- Cars 1
	(2, 13, 'Mater', 'Lead', NULL),											-- Cars 1
	(3, 8, 'Lightning McQueen', 'Lead', NULL),								-- Cars 2
	(3, 12, 'Sally Carrera', 'Secondary', NULL),							-- Cars 2
	(3, 13, 'Mater', 'Lead', NULL),											-- Cars 2
	(3, 4, 'Finn McMissile', 'Secondary', NULL),							-- Cars 2
	(3, 14, 'Holley Shiftwell', 'Secondary', NULL),							-- Cars 2
	(3, 15, 'Francesco Bernoulli', 'Secondary', NULL),						-- Cars 2
	(4, 8, 'Lightning McQueen', 'Lead', NULL),								-- Cars 3
	(4, 12, 'Sally Carrera', 'Secondary', NULL),							-- Cars 3
	(4, 13, 'Mater', 'Lead', NULL),											-- Cars 3
	(4, 16, 'Cruz Ramirez', 'Lead', NULL),									-- Cars 3
	(4, 17, 'Jackson Storm', 'Antagonist', NULL),							-- Cars 3
	(5, 18, 'Dom Cobb', 'Lead', 
			'You''re waiting for a train. A train that will 
			take you far away. You know where you hope the 
			train will take you, but you can''t know for sure. 
			Yet it doesn''t matter. Now, tell me why?'),					-- Inception
	(5, 19, 'Saito', 'Lead', NULL),											-- Inception
	(5, 20, 'Arthur', 'Secondary', NULL),									-- Inception
	(5, 21, 'Mal', 'Antagonist', 
			'Because we''ll be together!'),									-- Inception
	(5, 22, 'Eames', 'Secondary', NULL),									-- Inception
	(5, 23, 'Robert Fischer Jr.', 'Secondary', NULL)						-- Inception
------------------------------------------------------------------------------------------
	
	
------------------------------------------------------------------------------------------
INSERT INTO MovieDetails(MovieID, Description, Budget, BoxOffice, MPAARating, StudioID) VALUES
	(1, 'When Earth becomes uninhabitable in the future, a farmer 
		and ex-NASA pilot, Joseph Cooper, is tasked to pilot a 
		spacecraft, along with a team of researchers, to find a new 
		planet for humans.', 165000000, 715000000, NULL, 1),					-- Interstellar
	(2, 'On the way to the biggest race of his life, a hotshot rookie 
		race car gets stranded in a rundown town and learns that winning 
		isn''t everything in life.', 120000000, 462000000, 'G', 3),				-- Cars 1
	(3, 'Star race car Lightning McQueen and his pal Mater head 
		overseas to compete in the World Grand Prix race. But the road 
		to the championship becomes rocky as Mater gets caught up in an 
		intriguing adventure of his own: international espionage.',				
		200000000, 559800000, 'G', 3),											-- Cars 2
	(4, 'Lightning McQueen sets out to prove to a new generation of 
		racers that he''s still the best race car in the world.',				
		175000000, 383900000, 'G', 3),											-- Cars 3
	(5, 'A thief who steals corporate secrets through the use of 
		dream-sharing technology is given the inverse task of planting 
		an idea into the mind of a C.E.O., but his tragic past may doom 
		the project and his team to disaster.', 160000000, 839000000,			
		'PG-13', 4)																-- Inception
------------------------------------------------------------------------------------------
		
		
------------------------------------------------------------------------------------------
INSERT INTO Series(Title) VALUES
	('Loki'),
	('Squid Game'),
	('What if...?')
------------------------------------------------------------------------------------------


------------------------------------------------------------------------------------------
INSERT INTO SeriesGenres(SeriesID, GenreID) VALUES
	(1, 1), (1, 2), (1, 3), (1, 12),
	(2, 2), (2, 7), (2, 8),
	(3, 2), (3, 3), (3, 13)
------------------------------------------------------------------------------------------


------------------------------------------------------------------------------------------
INSERT INTO SeriesRoles(SeriesID, ActorID, CharacterName, Role) VALUES
	(1, 5, 'Loki', 'Lead'),										-- Loki
	(1, 7, 'Sylvie', 'Lead'),									-- Loki
	(1, 8, 'Mobius M. Mobius', 'Lead'),							-- Loki
	(1, 9, 'He Who Remains/Kang', 'Villain'),					-- Loki
	(1, 10, 'Ravonna Renslayer', 'Secondary'),					-- Loki
	(2, 48, 'Seong Gi-hun', 'Lead'),							-- Squid Game
	(2, 49, 'Cho Sang-woo', 'Antagonist'),						-- Squid Game
	(2, 50, 'Hwang Jun-ho', 'Secondary'),						-- Squid Game
	(2, 51, 'Kang Sae-byeok', 'Lead'),							-- Squid Game
	(2, 52, 'Oh Il-nam', 'Secondary'),							-- Squid Game
	(2, 53, 'Hwang In-ho / Front Man', 'Villain'),				-- Squid Game
	(3, 5, 'Loki', 'Secondary'),								-- What if...?
	(3, 35, 'Thor', 'Lead')										-- What if...?
------------------------------------------------------------------------------------------


------------------------------------------------------------------------------------------
INSERT INTO SeriesDetails(SeriesID, Description, Budget, BoxOffice, MPAARating, StudioID) VALUES
	(1, 'The mercurial villain Loki resumes his role as the God of Mischief 
		in a new series that takes place after the events of ''Avengers: Endgame''.',
		141000000, NULL, 'PG', 2),
	(2, 'Hundreds of cash-strapped players accept a strange invitation to 
		compete in children''s games. Inside, a tempting prize awaits with deadly 
		high stakes: a survival game that has a whopping 45.6 billion-won prize at 
		stake.', 21400000, 891100000, 'R', 10),
	(3, 'Exploring pivotal moments from the Marvel Cinematic Universe and turning 
		them on their head, leading the audience into uncharted territory.',
		150000000, NULL, 'PG', 2)
------------------------------------------------------------------------------------------


------------------------------------------------------------------------------------------
INSERT INTO Seasons(SeriesID, SeasonNumber, DirectorID, LaunchDate) VALUES
	(1, 1, 2, '20210714'),						-- Loki Season 1					-- 1
	(1, 2, 2, '20231005'),						-- Loki Season 2					-- 2
	(2, 1, 12, '20210917'),						-- Squid Game						-- 3
	(3, 1, 13, '20210821')						-- What if...?						-- 4
------------------------------------------------------------------------------------------


------------------------------------------------------------------------------------------
INSERT INTO Episodes(SeasonID, EpisodeNumber, Title, Description) VALUES
	(1, 1, 'Glorious Purpose', 
		'Loki, the God of Mischief, finds himself out of time 
		and in an unusual place and forced - against his godly disposition - 
		to cooperate with others.'),										-- Loki Season 1
	(1, 2, 'The Variant', 
		'Mobius puts Loki to work, but not everyone at TVA 
		is thrilled about the God of Mischief''s presence.'),				-- Loki Season 1
	(1, 3, 'Lamentis', 
		'Loki finds out The Variant''s plan, but he has his 
		own that will forever alter both their destinies.'),				-- Loki Season 1
	(1, 4, 'The Nexus Event', 
		'Frayed nerves and paranoia infiltrate the TVA as 
		Mobius and Hunter B-15 search for Loki and Sylvie.'),				-- Loki Season 1
	(1, 5, 'Journey Into Mystery', 
		'Loki tries to escape The Void, a desolate purgatory 
		where he meets variant versions of himself.'),						-- Loki Season 1
	(1, 6, 'For All Time. Always.', 
		'The clock is ticking in the season finale which finds 
		Loki and Sylvie on a date with destiny.'),							-- Loki Season 1
	(2, 1, 'Ouroboros', 
		'Loki finds himself lost to time and torn, quite literally, 
		between past, present and future.'),								-- Loki Season 2
	(2, 2, 'Breaking Brad',
		'With the TVA on the verge of a temporal meltdown, Loki and 
		Mobius will stop at nothing to find Sylvie.'),						-- Loki Season 2
	(2, 3, '1893',
		'Loki and Mobius go on the hunt for everyone''s favorite 
		cartoon clock as they try to save the TVA.'),						-- Loki Season 2
	(2, 4, 'Heart of the TVA',
		'The TVA''s Loom nears catastrophic failure, but Loki, 
		Mobius and Sylvie have a He Who Remains variant.'),					-- Loki Season 2
	(2, 5, 'Science/Fiction',
		'Loki traverses dying timelines in an attempt to find his 
		friends, but Reality is not what it seems.'),						-- Loki Season 2
	(2, 6, 'Glorious Purpose',
		'Loki learns the true nature of ''glorious purpose'' as 
		he rectifies the past.'),											-- Loki Season 2
	(3, 1, 'Red Light, Green Light',
		'Hoping to win easy money, a broke and desperate Gi-hun agrees 
		to take part in an enigmatic game. Not long into the first round, 
		unforeseen horrors unfold.'),										-- Squid Game
	(3, 2, 'Hell',
		'Split on whether to continue or quit, the group holds a vote. 
		But their realities in the outside world may prove to be just 
		as unforgiving as the game.'),										-- Squid Game
	(3, 3, 'The Man with the Umbrella',
		'A few players enter the next round - which promises equal doses 
		of sweet and deadly - with hidden advantages. Meanwhile, Jun-ho 
		sneaks his way inside.'),											-- Squid Game
	(3, 4, 'Stick to the Team',
		'As alliances form among the players, no one is safe in the dorm 
		after lights-out. The third game challenges Gi-hun''s team to think 
		strategically.'),													-- Squid Game
	(3, 5, 'A Fair World',
		'Gi-hun and his team take turns keeping guard through the night. 
		The masked men encounter trouble with their co-conspirators.'),		-- Squid Game
	(3, 6, 'Gganbu',
		'Players pair off for the fourth game. Gi-hun grapples with a 
		moral dilemma, Sang-woo chooses self-preservation and Sae-byeok 
		shares her untold story.'),											-- Squid Game
	(3, 7, 'VIPS',
		'The Masked Leader welcomes VIP guests to the facility for a 
		front-row viewing of the show. In the fifth game, some players 
		crack under pressure.'),											-- Squid Game
	(3, 8, 'Front Man',
		'Ahead of the last round, distrust and disgust run deep among the 
		finalists. Jun-ho makes a getaway, determined to expose the 
		game''s dirty secrets.'),											-- Squid Game
	(3, 9, 'One Lucky Day',
		'The final round presents another cruel test but this time, 
		how it ends depends on just one player. The game''s creator steps 
		out of the shadows.'),												-- Squid Game
	(4, 1, 'What If... Captain Carter Were the First Avenger?',
		'When Steve Rogers is seriously injured, Peggy Carter becomes 
		the world''s first super soldier.')									-- What if...?
------------------------------------------------------------------------------------------


------------------------------------------------------------------------------------------
INSERT INTO Critics(Name, Contact) VALUES
	('Rotten Tomatoes', NULL),							-- 1
	('IMDB', NULL),										-- 2
	('Metacritic', NULL),								-- 3
	('The Guardian', NULL)								-- 4
------------------------------------------------------------------------------------------


------------------------------------------------------------------------------------------
INSERT INTO MovieRatings(MovieID, CriticID, Rating, Description) VALUES
	(1, 1, 7.3, 
		'Interstellar represents more of the thrilling,
		thought-provoking, and visually resplendent filmmaking
		moviegoers have come to expect from writer-director Christopher Nolan, 
		even if its intellectual reach somewhat exceeds its grasp.'),							-- Interstellar
	(1, 2, 8.3, NULL),																			-- Interstellar
	(1, 3, 7.4, 
		'Nolan reaches for the stars in spectacular fashion, delivering 
		a mesmerising sci-fi epic that, despite a testing running time and 
		few too many flights of fancy, is grounded by an on-form McConaughey.'),				-- Interstellar
	(2, 2, 7.2, 
		'Pretty awesome movie, can''t beat the classics, definitely 
		one to rewatch. You don''t have to have an interest in cars to enjoy 
		this movie! Plot is moving, exciting ride.'),											-- Cars 1
	(2, 3, 7.3,
		'Cars is a fine example of the formula, with pleasant chemistry, the patented 
		Pixar cleverness, and the usual sweetly melancholy nostalgia courtesy of 
		songwriter Randy Newman.'),																-- Cars 1
	(2, 1, 7.5,
		'Cars offers visual treats that more than compensate for its somewhat 
		thinly written story, adding up to a satisfying diversion for younger viewers.'),		-- Cars 1
	(3, 1, 4.0,
		'Cars 2 is as visually appealing as any other Pixar production, 
		but all that dazzle can''t disguise the rusty storytelling under the hood.'),			-- Cars 2
	(3, 2, 6.2,
		'Cars 2 is not so good but, also not so bad, has some good parts but 
		the rest ... is not what I expected from pixar.'),										-- Cars 2
	(3, 3, 5.7, NULL),																			-- Cars 2
	(4, 1, 6.9,
		'Cars 3 has an unexpectedly poignant story to go with its dazzling animation, 
		suggesting Pixar''s most middle-of-the-road franchise may have a surprising 
		amount of tread left.'),																-- Cars 3
	(4, 2, 6.7,
		'It follows Lightning McQueen in a completely different role, that of 
		the aging star who is out of touch with the times. It is heartbreaking, 
		it is dramatic and it is compelling.'),													-- Cars 3
	(4, 3, 5.9, NULL),																			-- Cars 3
	(5, 2, 8.8, 
		'When you wake up from a good dream, you feel the reality is harsh. When you 
		wake up after a bad dream, you will be sentimentally attached to the beauty of 
		reality. But as long as life is good, reality and dreams don''t matter.'),				-- Inception
	(5, 1, 8.7,
		'Smart, innovative, and thrilling, Inception is that rare summer blockbuster 
		that succeeds viscerally as well as intellectually.')									-- Inception
------------------------------------------------------------------------------------------

	
------------------------------------------------------------------------------------------
INSERT INTO SeasonRatings(SeasonID, CriticID, Rating, Description) VALUES
	(1, 1, 9.2, 
		'A delightful diversion from the MCU as we know it, 
		Loki successfully sees star Tom Hiddleston leap from 
		beloved villain to endearing antihero -- with a little help 
		from Owen Wilson -- in a series that''s as off-kilter, charming, 
		and vaguely dangerous as the demigod himself.'),										-- Loki Season 1
	(1, 3, 7.0, 
		'So far, "Loki" is fun and zippy, reminiscent of the more casual 
		and comedic Marvel films such as "Thor: Ragnarok" and "Ant-Man."'),						-- Loki Season 2
	(3, 2, 8.0, NULL),																			-- Squid Game
	(3, 1, 9.5,
		'Squid Game''s unflinching brutality is not for the faint of heart, 
		but sharp social commentary and a surprisingly tender core will keep 
		viewers glued to the screen - even if it''s while watching between 
		their fingers.'),																		-- Squid Game
	(3, 3, 6.9, NULL)																			-- Squid Game
------------------------------------------------------------------------------------------

