create database Briose
go
use Briose
go

create table Cofetarie (
	id int primary key identity,
	name varchar(20) not null,
	score int not null
);

create table Briose (
	id int primary key identity,
	id_cofetarie int foreign key references Cofetarie(id) not null,
	marime int not null,
	pret float not null
);

alter table Briose
add culoare varchar(20);

insert into Cofetarie(name, score) values 
	('Mandra', 30),
	('Lemnul verde', 25),
	('Pralina', 28);

insert into Briose(id_cofetarie, marime, pret, culoare) values 
	(1, 12, 23, 'roz'),
	(1, 14, 27, 'albrastra'),
	(3, 8, 20, 'rosie'),
	(3, 10, 30, 'galbena'),
	(3, 18, 14, 'roz');

go
create or alter procedure tran_1 as begin
	begin tran;

	select * from Briose;

	waitfor delay '00:00:10';
	
	select * from Briose;
	
	waitfor delay '00:00:05';
	
	select * from Briose;
	commit tran;
end
go

create or alter procedure proc_problem as begin
	set transaction isolation level read uncommitted
	exec tran_1;
end
go

create or alter procedure proc_solution as begin
	set transaction isolation level read committed
	exec tran_1;
end
go

--exec proc_problem
exec proc_solution

create index idx_pret_briose on Briose (pret, culoare) include (marime);

select pret, marime from Briose order by pret;