-- Creating all tables for Cinematography DB

------------------------------------------------------------------------------------------
GO
ALTER DATABASE Cinematography SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
GO

USE master
GO
DROP DATABASE Cinematography
GO
CREATE DATABASE Cinematography
GO
USE Cinematography
GO
------------------------------------------------------------------------------------------


------------------------------------------------------------------------------------------
CREATE TABLE Versions (
	versionId INT PRIMARY KEY
)
------------------------------------------------------------------------------------------


------------------------------------------------------------------------------------------
CREATE TABLE Genres (
	GenreID INT PRIMARY KEY IDENTITY(1, 1),
	Name VARCHAR(50) NOT NULL
)
------------------------------------------------------------------------------------------


------------------------------------------------------------------------------------------
CREATE TABLE Directors (
	DirectorID INT PRIMARY KEY IDENTITY(1, 1),
	Name VARCHAR(50) NOT NULL,
	DOB Date,
	Nationality VARCHAR(50) NOT NULL,
	Contact VARCHAR(50)
)
------------------------------------------------------------------------------------------


------------------------------------------------------------------------------------------
CREATE TABLE Studios (
	StudioID INT PRIMARY KEY IDENTITY(1, 1),
	Name VARCHAR(50) NOT NULL,
	Location VARCHAR(80)
)
------------------------------------------------------------------------------------------


------------------------------------------------------------------------------------------
CREATE TABLE Actors (
	ActorID INT PRIMARY KEY IDENTITY(1, 1),
	Name VARCHAR(50) NOT NULL,
	DOB Date,
	Nationality VARCHAR(50) NOT NULL,
	Contact VARCHAR(50)
)
------------------------------------------------------------------------------------------


------------------------------------------------------------------------------------------
CREATE TABLE Movies (
	MovieID INT PRIMARY KEY IDENTITY(1, 1),
	Title VARCHAR(80) NOT NULL,
	DirectorID INT FOREIGN KEY REFERENCES Directors(DirectorID) NOT NULL,
	LaunchDate Date,
	RunningTime INT NOT NULL
)
------------------------------------------------------------------------------------------


------------------------------------------------------------------------------------------
CREATE TABLE MoviesGenres (
	MovieID INT FOREIGN KEY REFERENCES Movies(MovieID),
	GenreID INT FOREIGN KEY REFERENCES Genres(GenreID),
	CONSTRAINT pk_MGID PRIMARY KEY (MovieID, GenreID)
)
------------------------------------------------------------------------------------------


------------------------------------------------------------------------------------------
CREATE TABLE MovieRoles (
	MovieID INT FOREIGN KEY REFERENCES Movies(MovieID),
	ActorID INT FOREIGN KEY REFERENCES Actors(ActorID),
	CONSTRAINT pk_MRID PRIMARY KEY (ActorID, MovieID),
	CharacterName VARCHAR(50),
	Role VARCHAR(30) CHECK (Role='Lead' or 
				Role='Secondary' or 
				Role='Narrator' or
				Role='Villain' or
				Role='Antagonist') NOT NULL,
	FamousLine VARCHAR(255)
)
------------------------------------------------------------------------------------------


------------------------------------------------------------------------------------------
CREATE TABLE MovieDetails (
	MovieID INT FOREIGN KEY REFERENCES Movies(MovieID),
	CONSTRAINT pk_MovieDetails PRIMARY KEY(MovieID),
	Description VARCHAR(350),
	Budget BIGINT CHECK (Budget >= 0) NOT NULL,
	BoxOffice BIGINT CHECK (BoxOffice >= 0) NOT NULL,
	MPAARating VARCHAR(20) CHECK (MPAARating='G' or 
								MPAARating='PG' or 
								MPAARating='PG-13' or 
								MPAARating='R' or 
								MPAARating='NC-17'),
	StudioID INT FOREIGN KEY REFERENCES Studios(StudioID)
)
------------------------------------------------------------------------------------------


------------------------------------------------------------------------------------------
CREATE TABLE Series (
	SeriesID INT PRIMARY KEY IDENTITY(1, 1),
	Title VARCHAR(80) NOT NULL
)
------------------------------------------------------------------------------------------


------------------------------------------------------------------------------------------
CREATE TABLE SeriesGenres (
	SeriesID INT FOREIGN KEY REFERENCES Series(SeriesID),
	GenreID INT FOREIGN KEY REFERENCES Genres(GenreID),
	CONSTRAINT pk_SGID PRIMARY KEY (SeriesID, GenreID)
)
------------------------------------------------------------------------------------------


------------------------------------------------------------------------------------------
CREATE TABLE SeriesRoles (
	SeriesID INT FOREIGN KEY REFERENCES Series(SeriesID),
	ActorID INT FOREIGN KEY REFERENCES Actors(ActorID),
	CONSTRAINT pk_ASID PRIMARY KEY (ActorID, SeriesID),
	CharacterName VARCHAR(50),
	Role VARCHAR(30) CHECK (Role='Lead' or
				Role='Secondary' or 
				Role='Narrator' or
				Role='Villain' or
				Role='Antagonist') NOT NULL 
)
------------------------------------------------------------------------------------------


------------------------------------------------------------------------------------------
CREATE TABLE SeriesDetails (
	SeriesID INT FOREIGN KEY REFERENCES Series(SeriesID),
	CONSTRAINT pk_SeriesDetails PRIMARY KEY(SeriesID),
	Description VARCHAR(250),
	Budget BIGINT CHECK (Budget >= 0) NOT NULL,
	BoxOffice BIGINT CHECK (BoxOffice >= 0),
	MPAARating VARCHAR(20) CHECK (MPAARating='G' or 
								MPAARating='PG' or 
								MPAARating='PG-13' or 
								MPAARating='R' or 
								MPAARating='NC-17'),
	StudioID INT FOREIGN KEY REFERENCES Studios(StudioID)
)
------------------------------------------------------------------------------------------


------------------------------------------------------------------------------------------
CREATE TABLE Seasons (
	SeasonID INT PRIMARY KEY IDENTITY(1, 1),
	SeriesID INT FOREIGN KEY REFERENCES Series(SeriesID) NOT NULL,
	SeasonNumber INT NOT NULL,
	DirectorID INT FOREIGN KEY REFERENCES Directors(DirectorID) NOT NULL,
	LaunchDate Date,
)
------------------------------------------------------------------------------------------


------------------------------------------------------------------------------------------
CREATE TABLE Episodes (
	EpisodeID INT PRIMARY KEY IDENTITY(1, 1),
	SeasonID INT FOREIGN KEY REFERENCES Seasons(SeasonID) NOT NULL,
	EpisodeNumber INT NOT NULL,
	Title VARCHAR(80),
	Description VARCHAR(250)
)
------------------------------------------------------------------------------------------


------------------------------------------------------------------------------------------
CREATE TABLE Critics (
	CriticID INT PRIMARY KEY IDENTITY(1, 1),
	Name VARCHAR(50) NOT NULL,
	Contact VARCHAR(80)
)
------------------------------------------------------------------------------------------


------------------------------------------------------------------------------------------
CREATE TABLE MovieRatings (
	MovieID INT FOREIGN KEY REFERENCES Movies(MovieID),
	CriticID INT FOREIGN KEY REFERENCES Critics(CriticID),
	CONSTRAINT pk_MRateID PRIMARY KEY (MovieID, CriticID),
	Rating FLOAT CHECK (Rating>=0.0 AND Rating<=10.0) NOT NULL,
	Description VARCHAR(350)
)
------------------------------------------------------------------------------------------


------------------------------------------------------------------------------------------
CREATE TABLE SeasonRatings (
	SeasonID INT FOREIGN KEY REFERENCES Seasons(SeasonID),
	CriticID INT FOREIGN KEY REFERENCES Critics(CriticID),
	CONSTRAINT pk_SRateID PRIMARY KEY (SeasonID, CriticID),
	Rating FLOAT CHECK (Rating>=0.0 AND Rating<=10.0) NOT NULL,
	Description VARCHAR(350)
)
------------------------------------------------------------------------------------------
