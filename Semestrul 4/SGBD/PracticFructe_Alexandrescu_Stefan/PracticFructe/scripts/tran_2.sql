use S10
go

create or alter procedure proc_tran_2 as begin
	begin tran;

	update Fructe set Denumire = 'Struguri' where Fid = 1;
	waitfor delay '00:00:10';

	commit tran;

	select * from Fructe;
end
go

exec proc_tran_2;
