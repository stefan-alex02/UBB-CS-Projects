use Briose
go

create or alter procedure tran_2 as begin
	begin tran;

	update Briose set culoare='turcoaz' where id=1;

	waitfor delay '00:00:10';

	rollback tran;
end
go

exec tran_2;