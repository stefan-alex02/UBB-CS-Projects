package org.example.modelpractic.persistence.pagingrepos;

import org.example.modelpractic.domain.Person;
import org.example.modelpractic.persistence.DatabaseConnection;
import org.example.modelpractic.persistence.dbrepos.PersonDBRepository;
import org.example.modelpractic.persistence.paging.Page;
import org.example.modelpractic.persistence.paging.PageImplementation;
import org.example.modelpractic.persistence.paging.Pageable;
import org.example.modelpractic.persistence.paging.PagingRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class PersonDBPagingRepository extends PersonDBRepository implements PagingRepository<Long, Person, Long> {
    public PersonDBPagingRepository(DatabaseConnection databaseConnection) {
        super(databaseConnection);
    }

    @Override
    public Page<Person> findAllWhere(Pageable pageable, Long havingValue) {
        Set<Person> entities = new HashSet<>();

        try {
            Connection connection = databaseConnection.getConnection();
            PreparedStatement statement =
                    connection.prepareStatement("""
                            select P.id, P.username, P.name, D.person_id, D.indicativ_masina from persons P
                            inner join orders O on O.person_id = P.id
                            left join drivers D on D.person_id = P.id where O.driver_id = ?
                            group by P.id, P.username, P.name, D.person_id, D.indicativ_masina
                            limit ? offset ?""");
            statement.setLong(1, havingValue);
            statement.setInt(2, pageable.getPageSize());
            statement.setInt(3, (pageable.getPageNumber() - 1) * pageable.getPageSize());

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next())
            {
                Person person = getPerson(resultSet);
                entities.add(person);
            }
            return new PageImplementation<>(pageable, entities.stream());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
