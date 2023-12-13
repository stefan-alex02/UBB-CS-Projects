package ir.map.g221.guisocialnetwork.persistence.pagingrepos;

import ir.map.g221.guisocialnetwork.domain.entities.Entity;
import ir.map.g221.guisocialnetwork.domain.entities.User;
import ir.map.g221.guisocialnetwork.persistence.DatabaseConnection;
import ir.map.g221.guisocialnetwork.persistence.Repository;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public interface PagingRepository<ID, E extends Entity<ID>> extends Repository<ID, E> {
    DatabaseConnection databaseConnection = DatabaseConnection.getSingleInstance();

    default Page<E> findAll(Pageable pageable) {
        Set<E> entities = new HashSet<>();

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement("select * from " + getTableName() + " limit ? offset ?");
        ) {
            statement.setInt(1, pageable.getPageSize());
            statement.setInt(2, (pageable.getPageNumber() - 1) * pageable.getPageSize());

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next())
            {
                E entity = createEntityFrom(resultSet);
                entities.add(entity);
            }
            return new PageImplementation<>(pageable, entities.stream());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
