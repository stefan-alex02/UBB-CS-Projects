USE Cinematography
GO

CREATE OR ALTER PROCEDURE PR_Create_Log_Table AS
BEGIN
	IF not exists (select * from sysobjects where name='LogTable' and xtype='U') BEGIN
    CREATE TABLE LogTable(
		Lid INT IDENTITY PRIMARY KEY,
		TypeOperation VARCHAR(50),
		TableOperation VARCHAR(50),
		ExecutionDate DATETIME
	)
	END
END
GO

CREATE OR ALTER PROCEDURE PR_Dirty_Reads_2 AS
BEGIN
	EXEC PR_Create_Log_Table;
	BEGIN TRAN 

	UPDATE Movies SET Title='Not Interstellar' WHERE MovieID = 1;
	WAITFOR DELAY '00:00:10' 
	UPDATE Movies SET Title='Also not Interstellar' WHERE MovieID = 1;

	ROLLBACK TRAN 

	INSERT INTO LogTable (TypeOperation, TableOperation, ExecutionDate)
            VALUES ('UPDATE (ROLLBACK)', 'Movies', GETDATE());
END
GO

CREATE OR ALTER PROCEDURE PR_Unrepeatable_Reads_2 AS
BEGIN
	EXEC PR_Create_Log_Table;
	BEGIN TRAN 

	WAITFOR DELAY '00:00:05'
	UPDATE Movies SET Title='Cars 5' WHERE MovieID = 2;

	COMMIT TRAN 
	
	INSERT INTO LogTable (TypeOperation, TableOperation, ExecutionDate)
            VALUES ('UPDATE', 'Movies', GETDATE());
END
GO

CREATE OR ALTER PROCEDURE PR_Phantom_Reads_2 AS
BEGIN
	EXEC PR_Create_Log_Table;
	BEGIN TRAN 

	WAITFOR DELAY '00:00:05'
	INSERT INTO Movies (Title, DirectorID, LaunchDate, RunningTime) VALUES
		('Incredibles', 3, '20041024', 123);

	COMMIT TRAN 

	INSERT INTO LogTable (TypeOperation, TableOperation, ExecutionDate)
            VALUES ('INSERT', 'Movies', GETDATE());
END
GO

CREATE OR ALTER PROCEDURE PR_Deadlock_Tran_2 AS
BEGIN
	BEGIN TRAN
	
	UPDATE Actors SET Name = 'Jason Statham 2' where ActorID = 1;
	INSERT INTO LogTable (TypeOperation, TableOperation, ExecutionDate)
            VALUES ('UPDATE', 'Actors', GETDATE());

	WAITFOR DELAY '00:00:05';

	UPDATE Movies SET Title = 'Incredibles 2' where MovieId = 1;
	INSERT INTO LogTable (TypeOperation, TableOperation, ExecutionDate)
            VALUES ('UPDATE', 'Movies', GETDATE());

	COMMIT TRAN
	
	WAITFOR DELAY '00:00:06';
	SELECT * FROM Movies;
	SELECT * FROM Actors;
END
GO

--EXEC PR_Dirty_Reads_2;
--EXEC PR_Unrepeatable_Reads_2;
--EXEC  PR_Phantom_Reads_2;

EXEC PR_Deadlock_Tran_2;
