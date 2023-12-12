// See https://aka.ms/new-console-template for more information

using sem11.domain;
using sem11.repository;

Console.WriteLine("Hello, World!");


Angajat a1 = new Angajat()
{
    Level = KnowledgeLevel.Senior,
    Nume = "Ceva",
    Id = "2",
    VenitPeOra = 10
};

List<Angajat> angajati = new List<Angajat>() {
    a1,
    new Angajat()
    {
        Level = KnowledgeLevel.Junior,
        Nume = "Florin",
        Id = "3",
        VenitPeOra = 6
    }
};

InMemoryRepository<string, Angajat> repository = new ();






