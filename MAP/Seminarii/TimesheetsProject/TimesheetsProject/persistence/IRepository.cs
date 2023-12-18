using TimesheetsProject.domain;

namespace TimesheetsProject.persistence;

interface IRepository<ID, E> where E : HasId<ID>
{
    E FindOne(ID id);
    IEnumerable<E> FindAll();
    E Save(E entity);
    E Delete(ID id);
    E Update(E entity);
}