USE Cinematography
GO

---------------------------------------------------------------------------------------
CREATE OR ALTER FUNCTION Validate_SeriesTitle (@title varchar(80)) RETURNS INT AS
BEGIN
	DECLARE @result INT = 1;
	IF @title IS NULL OR LEN(@title) <= 2 BEGIN
		SET @result = 0
	END
	RETURN @result
END
GO
---------------------------------------------------------------------------------------

---------------------------------------------------------------------------------------
CREATE OR ALTER FUNCTION Validate_ActorName (@name varchar(80)) RETURNS INT AS
BEGIN
	DECLARE @result INT = 1;
	IF @name IS NULL OR LEN(@name) <= 3 BEGIN
		SET @result = 0
	END
	RETURN @result
END
GO
---------------------------------------------------------------------------------------

---------------------------------------------------------------------------------------
CREATE OR ALTER FUNCTION Validate_ActorDOB (@date DATE) RETURNS INT AS
BEGIN
	DECLARE @result INT = 1;
	IF @date IS NOT NULL AND ( @date <= '1700-01-01' OR @date >= '2399-12-31') BEGIN
		SET @result = 0
	END
	RETURN @result
END
GO
---------------------------------------------------------------------------------------

---------------------------------------------------------------------------------------
CREATE OR ALTER FUNCTION Validate_ActorNationality (@nationality varchar(80)) RETURNS INT AS
BEGIN
	DECLARE @result INT = 1;
	IF @nationality IS NULL OR @nationality NOT IN 
			('English', 'British', 'American', 'Romanian', 'German', 'French', 
			'Australian', 'Spanish', 'Danish', 'Canadian', 'South Korean') BEGIN
		SET @result = 0
	END
	RETURN @result
END
GO
---------------------------------------------------------------------------------------

---------------------------------------------------------------------------------------
CREATE OR ALTER FUNCTION Validate_SeriesRolesCharacterName (@characterName varchar(80)) RETURNS INT AS
BEGIN
	DECLARE @result INT = 1;
	IF @characterName IS NULL OR LEN(@characterName) <= 2 BEGIN
		SET @result = 0
	END
	RETURN @result
END
GO
---------------------------------------------------------------------------------------

---------------------------------------------------------------------------------------
CREATE OR ALTER FUNCTION Validate_SeriesRolesCharacterName (@characterName varchar(80)) RETURNS INT AS
BEGIN
	DECLARE @result INT = 1;
	IF @characterName IS NOT NULL AND LEN(@characterName) <= 2 BEGIN
		SET @result = 0
	END
	RETURN @result
END
GO
---------------------------------------------------------------------------------------

---------------------------------------------------------------------------------------
CREATE OR ALTER FUNCTION Validate_SeriesRolesRole (@role varchar(80)) RETURNS INT AS
BEGIN
	DECLARE @result INT = 1;
	IF @role IS NULL OR @role NOT IN ('Lead', 'Secondary', 'Narrator', 'Villain', 'Antagonist') BEGIN
		SET @result = 0
	END
	RETURN @result
END
GO
---------------------------------------------------------------------------------------

---------------------------------------------------------------------------------------
CREATE OR ALTER PROCEDURE proc_TryCreateLogTable AS
BEGIN
	IF not exists (select * from sysobjects where name='LogTable' and xtype='U') BEGIN
        CREATE TABLE LogTable(
			Lid INT IDENTITY PRIMARY KEY,
			TypeOperation VARCHAR(50),
			TableOperation VARCHAR(50),
			ExecutionDate DATETIME		)	ENDEND
GO
---------------------------------------------------------------------------------------
---------------------------------------------------------------------------------------CREATE OR ALTER PROCEDURE AddSeriesActor 
	@seriesTitle varchar(80), 
	@actorName varchar(80), 
	@actorDOB DATE, 
	@actorNationality varchar(80), 
	@actorContact varchar(100), 
	@characterName varchar(80), 
	@role varchar(80)
AS
BEGIN
	EXEC proc_TryCreateLogTable;

	BEGIN TRAN
	BEGIN TRY
		IF(dbo.Validate_SeriesTitle(@seriesTitle) <> 1) BEGIN
			RAISERROR('Series title is null or too short', 14, 1)
		END

		INSERT INTO Series (Title) VALUES (@seriesTitle)


		IF(dbo.Validate_ActorName(@actorName) <> 1) BEGIN
			RAISERROR('Actor name is null or too short', 14, 1)
		END
		IF(dbo.Validate_ActorDOB(@actorDOB) <> 1) BEGIN
			RAISERROR('Actor date of birth is invalid', 14, 1)
		END
		IF(dbo.Validate_ActorNationality(@actorNationality) <> 1) BEGIN
			RAISERROR('Actor nationality is null or does not belong to available list', 14, 1)
		END
		
		INSERT INTO Actors (Name, DOB, Nationality, Contact) VALUES
				(@actorName, @actorDOB, @actorNationality, @actorContact)


		IF(dbo.Validate_SeriesRolesCharacterName(@characterName) <> 1) BEGIN
			RAISERROR('Character name is too short', 14, 1)
		END
		IF(dbo.Validate_SeriesRolesRole(@role) <> 1) BEGIN
			RAISERROR('Role type is null or does not belong to available role list', 14, 1)
		END

		DECLARE @actorID INT
		DECLARE @seriesID INT

		SELECT
			@actorID = (SELECT MAX(ActorID) FROM Actors),
			@seriesID = (SELECT MAX(SeriesID) FROM Series);
		
		INSERT INTO SeriesRoles (SeriesID, ActorID, CharacterName, Role) VALUES
			(@seriesID, @actorID, @characterName, @role)

		COMMIT TRAN

		INSERT INTO LogTable (TypeOperation, TableOperation, ExecutionDate) values
			('INSERT', 'Series', GETDATE()),
			('INSERT', 'Actors', GETDATE()),
			('INSERT', 'SeriesRoles', GETDATE())

		SELECT 'Transaction committed'
		END TRY
	BEGIN CATCH
		ROLLBACK TRAN

		-- Retrieve and print error details
		DECLARE @ErrorMessage NVARCHAR(4000);
		DECLARE @ErrorSeverity INT;
		DECLARE @ErrorState INT;
		DECLARE @ErrorProcedure NVARCHAR(200);
		DECLARE @ErrorLine INT;

		SET @ErrorMessage = ERROR_MESSAGE();
		SET @ErrorSeverity = ERROR_SEVERITY();
		SET @ErrorState = ERROR_STATE();
		SET @ErrorProcedure = ERROR_PROCEDURE();
		SET @ErrorLine = ERROR_LINE();

		-- Print error details
		SELECT 
			'Transaction rollbacked',
			'Error Message' = @ErrorMessage,
			'Error Severity' = @ErrorSeverity,
			'Error State' = @ErrorState,
			'Error Procedure' = @ErrorProcedure,
			'Error Line' = @ErrorLine
	END CATCH
END
GO
---------------------------------------------------------------------------------------

---------------------------------------------------------------------------------------
CREATE OR ALTER PROCEDURE AddSeriesActorMultipleTrans 
    @seriesTitle VARCHAR(80), 
    @actorName VARCHAR(80), 
    @actorDOB DATE, 
    @actorNationality VARCHAR(80), 
    @actorContact VARCHAR(100), 
    @characterName VARCHAR(80), 
    @role VARCHAR(80)
AS
BEGIN
    EXEC proc_TryCreateLogTable;

	DECLARE @ErrorMessage NVARCHAR(400)

    -- Insert into Series table
    BEGIN TRAN SeriesTran
    BEGIN TRY
        IF dbo.Validate_SeriesTitle(@seriesTitle) <> 1 
        BEGIN
            RAISERROR('Series title is null or too short', 14, 1);
        END
        INSERT INTO Series (Title) VALUES (@seriesTitle);
        COMMIT TRAN SeriesTran;

        INSERT INTO LogTable (TypeOperation, TableOperation, ExecutionDate) 
            VALUES ('INSERT', 'Series', GETDATE());
    END TRY
    BEGIN CATCH
        ROLLBACK TRAN SeriesTran;

        SET @ErrorMessage = ERROR_MESSAGE();
        SELECT 'Series insertion failed:', @ErrorMessage;
    END CATCH;

    -- Insert into Actors table
    BEGIN TRAN ActorsTran
    BEGIN TRY
        IF dbo.Validate_ActorName(@actorName) <> 1
        BEGIN
            RAISERROR('Actor name is null or too short', 14, 1);
        END
        IF dbo.Validate_ActorDOB(@actorDOB) <> 1
        BEGIN
            RAISERROR('Actor date of birth is invalid', 14, 1);
        END
        IF dbo.Validate_ActorNationality(@actorNationality) <> 1
        BEGIN
            RAISERROR('Actor nationality is null or does not belong to available list', 14, 1);
        END
        
        INSERT INTO Actors (Name, DOB, Nationality, Contact) 
            VALUES (@actorName, @actorDOB, @actorNationality, @actorContact);
        COMMIT TRAN ActorsTran;

        INSERT INTO LogTable (TypeOperation, TableOperation, ExecutionDate)
            VALUES ('INSERT', 'Actors', GETDATE());
    END TRY
    BEGIN CATCH
        ROLLBACK TRAN ActorsTran;

        SET @ErrorMessage = ERROR_MESSAGE();
        SELECT 'Actors insertion failed:', @ErrorMessage;
    END CATCH;
	
    DECLARE @actorID INT;
    DECLARE @seriesID INT;

    SET @actorID = (SELECT MAX(ActorID) FROM Actors);
    SET @seriesID = (SELECT MAX(SeriesID) FROM Series);

    -- Insert into SeriesRoles table
    BEGIN TRAN SeriesRolesTran
    BEGIN TRY
        IF dbo.Validate_SeriesRolesCharacterName(@characterName) <> 1 
        BEGIN
            RAISERROR('Character name is too short', 14, 1);
        END
        IF dbo.Validate_SeriesRolesRole(@role) <> 1 
        BEGIN
            RAISERROR('Role type is null or does not belong to available role list', 14, 1);
        END
        
        INSERT INTO SeriesRoles (SeriesID, ActorID, CharacterName, Role) 
            VALUES (@seriesID, @actorID, @characterName, @role);
        COMMIT TRAN SeriesRolesTran;

        INSERT INTO LogTable (TypeOperation, TableOperation, ExecutionDate)
            VALUES ('INSERT', 'SeriesRoles', GETDATE());
    END TRY
    BEGIN CATCH
        ROLLBACK TRAN SeriesRolesTran;

        SET @ErrorMessage = ERROR_MESSAGE();
        SELECT 'SeriesRoles insertion failed:', @ErrorMessage;
    END CATCH;
END;

