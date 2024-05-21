USE Cinematography
GO

CREATE OR ALTER PROCEDURE PR_Transaction_1 AS
BEGIN
	BEGIN TRAN

	SELECT * FROM Movies;
	WAITFOR DELAY '00:00:10'
	SELECT * FROM Movies;

	COMMIT TRAN
	
	WAITFOR DELAY '00:00:05'
	SELECT * FROM Movies;
END
GO

CREATE OR ALTER PROCEDURE PR_1_Read_Uncommited AS
BEGIN
	SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED;
	EXEC PR_Transaction_1;
END
GO

CREATE OR ALTER PROCEDURE PR_1_Read_Commited AS
BEGIN
	SET TRANSACTION ISOLATION LEVEL READ COMMITTED;
	EXEC PR_Transaction_1;
END
GO

CREATE OR ALTER PROCEDURE PR_1_Repeatable_Read AS
BEGIN
	SET TRANSACTION ISOLATION LEVEL REPEATABLE READ;
	EXEC PR_Transaction_1;
	
	UPDATE Movies SET Title='Cars' WHERE MovieID = 2;
	DELETE FROM Movies WHERE Title = 'Incredibles';
END
GO

CREATE OR ALTER PROCEDURE PR_1_Serializable AS
BEGIN
	SET TRANSACTION ISOLATION LEVEL SERIALIZABLE;
	EXEC PR_Transaction_1;
	
	DELETE FROM Movies WHERE Title = 'Incredibles';
END
GO

CREATE OR ALTER PROCEDURE PR_Deadlock_Tran_1 AS
BEGIN
	UPDATE Movies SET Title = 'Interstellar' where MovieId = 1;
	UPDATE Actors SET Name = 'Matt Damon' where ActorID = 1;

	SET DEADLOCK_PRIORITY HIGH;
	BEGIN TRAN
	
	UPDATE Movies SET Title = 'Incredibles 1' where MovieId = 1;
	INSERT INTO LogTable (TypeOperation, TableOperation, ExecutionDate)
        VALUES ('UPDATE', 'Movies', GETDATE());

	WAITFOR DELAY '00:00:05';

	UPDATE Actors SET Name = 'Jason Statham 1' where ActorID = 1;
	INSERT INTO LogTable (TypeOperation, TableOperation, ExecutionDate)
            VALUES ('UPDATE', 'Actors', GETDATE());

	COMMIT TRAN
	
	SELECT * FROM Movies;
	SELECT * FROM Actors;
END
GO

CREATE OR ALTER PROCEDURE PR_Deadlock_Tran_1_Solution AS
BEGIN
	UPDATE Movies SET Title = 'Interstellar' where MovieId = 1;
	UPDATE Actors SET Name = 'Matt Damon' where ActorID = 1;

	SET DEADLOCK_PRIORITY HIGH;
	BEGIN TRAN
	
	UPDATE Actors SET Name = 'Jason Statham 1' where ActorID = 1;
	INSERT INTO LogTable (TypeOperation, TableOperation, ExecutionDate)
            VALUES ('UPDATE', 'Actors', GETDATE());

	WAITFOR DELAY '00:00:05';

	UPDATE Movies SET Title = 'Incredibles 1' where MovieId = 1;
	INSERT INTO LogTable (TypeOperation, TableOperation, ExecutionDate)
        VALUES ('UPDATE', 'Movies', GETDATE());

	COMMIT TRAN
	
	WAITFOR DELAY '00:00:06';
	SELECT * FROM Movies;
	SELECT * FROM Actors;
END
GO

--EXEC PR_1_Read_Uncommited;
EXEC PR_1_Read_Commited;
--EXEC PR_1_Repeatable_Read;
--EXEC PR_1_Serializable;

--EXEC PR_Deadlock_Tran_1;
--EXEC PR_Deadlock_Tran_1_Solution;

--SELECT * FROM LogTable;
