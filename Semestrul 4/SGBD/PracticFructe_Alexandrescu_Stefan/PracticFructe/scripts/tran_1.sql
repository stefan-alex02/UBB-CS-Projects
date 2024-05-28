use S10
go

create index idx_fructe_pret on Fructe (PretMediu) include (Denumire, Culoare, LunaOptimaCulegere);

select Denumire, Culoare, LunaOptimaCulegere, PretMediu 
from Fructe 
order by PretMediu desc;

go
create or alter procedure proc_tran_1 as begin
	begin tran;
	select * from Fructe;

	waitfor delay '00:00:10'
	
	select * from Fructe;
	commit tran;
end
go

create or alter procedure proc_problem as begin
	update Fructe set Denumire = 'Capsuni' where Fid = 1;
	set transaction isolation level read committed;
	exec proc_tran_1;
end
go

create or alter procedure proc_solution as begin
	update Fructe set Denumire = 'Capsuni' where Fid = 1;
	set transaction isolation level repeatable read;
	exec proc_tran_1;
end
go

--exec proc_problem;
exec proc_solution;