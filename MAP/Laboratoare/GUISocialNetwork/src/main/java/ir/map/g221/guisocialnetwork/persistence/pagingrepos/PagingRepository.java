package ir.map.g221.guisocialnetwork.persistence.pagingrepos;

import ir.map.g221.guisocialnetwork.domain.entities.Entity;
import ir.map.g221.guisocialnetwork.persistence.DatabaseConnection;
import ir.map.g221.guisocialnetwork.persistence.Repository;

import java.sql.*;

public interface PagingRepository<ID, E extends Entity<ID>> extends Repository<ID, E> {
    DatabaseConnection databaseConnection = DatabaseConnection.getSingleInstance();

    default Page<E> findAll(Pageable pageable) {
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement("select * from users limit ? offset ?");
        ) {
            statement.setInt(1, pageable.getPageSize());
            statement.setInt(2, (pageable.getPageNumber() - 1) * pageable.getPageSize());

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next())
            {
                Long id= resultSet.getLong("id");
                String firstName=resultSet.getString("first_name");
                String lastName=resultSet.getString("last_name");
                User user=new User(firstName,lastName);
                user.setId(id);
                users.add(user);

            }
            return new PageImplementation<>(pageable, users.stream());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
