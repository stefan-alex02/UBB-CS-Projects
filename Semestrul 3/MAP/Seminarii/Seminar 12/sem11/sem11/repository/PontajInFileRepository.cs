﻿using sem11.domain;
using sem11.domain.validators;

namespace sem11.repository;

class PontajInFileRepository : InFileRepository<string, Pontaj>
{
    public PontajInFileRepository(IValidator<Pontaj> vali, string fileName) : base(vali, fileName, null)
    {
        loadFromFile();
    }

    private new void loadFromFile()
    {


        List<Angajat> angajati =
            DataReader.ReadData<Angajat>("..\\..\\..\\data\\angajati.txt", EntityToFileMapping.CreateAngajat);
        //(
        //    ConfigurationManager.AppSettings["angajatiFileName"],
        //    EntityToFileMapping.CreateAngajat
        //);

        List<Sarcina> sarcini = DataReader.ReadData<Sarcina>
            ("..\\..\\..\\data\\sarcini.txt", EntityToFileMapping.CreateSarcina);
        //(
        //    ConfigurationManager.AppSettings["sarciniFileName"],
        //    EntityToFileMapping.CreateSarcina
        //);
        using (StreamReader sr = new StreamReader(fileName))
        {
            string line;
            while ((line = sr.ReadLine()) != null)
            {
                string[] fields = line.Split(','); // new char[] { ',' } 
                Angajat a = angajati.Find(x => x.Id.Equals(fields[0]));
                Sarcina s = sarcini.Find(x => x.Id.Equals(fields[1]));
                String id = fields[0] + "," + fields[1];
                Pontaj pontaj = new Pontaj()
                {
                    Id = id,
                    Angajat = a,
                    Sarcina = s,
                    Date = DateTime.ParseExact(fields[2], "d/M/yyyy", System.Globalization.CultureInfo.InvariantCulture)
                };
                base.entities[pontaj.Id] = pontaj;
            }
        }
    }

}