USE Cinematography
GO

select * from Series;
select * from Actors;
select * from SeriesRoles;


EXEC AddSeriesActor @seriesTitle = 'Loki', @actorName = 'Tom Hiddleston', @actorDOB = '1981-02-09', 
		@actorNationality = 'Englishhh', @actorContact = NULL, @characterName = 'Loki', @role = 'Lead'

EXEC AddSeriesActor @seriesTitle = 'Loki', @actorName = 'Tom Hiddleston', @actorDOB = '1981-02-09', 
		@actorNationality = 'English', @actorContact = NULL, @characterName = 'Loki', @role = 'Lead'



EXEC AddSeriesActorMultipleTrans @seriesTitle = 'Breaking Bad', @actorName = 'Bryan Cranston', @actorDOB = '1000-03-07', 
		@actorNationality = 'American', @actorContact = NULL, @characterName = 'Walter White', @role = 'Lead'

EXEC AddSeriesActorMultipleTrans @seriesTitle = 'Breaking Bad', @actorName = 'Bryan Cranston', @actorDOB = '1956-03-07', 
		@actorNationality = 'American', @actorContact = NULL, @characterName = 'Walter White', @role = 'Lead'


select * from LogTable;

delete from SeriesRoles;
delete from Series;
delete from Actors;
delete from LogTable;
