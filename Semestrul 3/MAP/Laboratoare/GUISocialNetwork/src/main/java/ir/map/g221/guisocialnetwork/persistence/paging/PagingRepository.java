package ir.map.g221.guisocialnetwork.persistence.paging;

import ir.map.g221.guisocialnetwork.domain.entities.Entity;
import ir.map.g221.guisocialnetwork.persistence.DatabaseConnection;
import ir.map.g221.guisocialnetwork.persistence.Repository;
import ir.map.g221.guisocialnetwork.persistence.customqueries.CustomQuery;
import ir.map.g221.guisocialnetwork.persistence.customqueries.MappableCustomQuery;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public interface PagingRepository<ID, E extends Entity<ID>> extends Repository<ID, E> {
    DatabaseConnection databaseConnection = DatabaseConnection.getSingleInstance();

    default Page<E> findAll(Pageable pageable) {
        Set<E> entities = new HashSet<>();

        try {
            Connection connection = databaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement("select * from " + getTableName() + " limit ? offset ?");
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

    default Page<E> findAllWhere(CustomQuery<ID, E> customQuery, Pageable pageable) {
        List<E> entities = new ArrayList<>();

        try {
            Connection connection = databaseConnection.getConnection();
            PreparedStatement statement =
                    connection.prepareStatement(customQuery.getQueryString() + " limit ? offset ?");

            customQuery.fillStatement(statement);

            int paramCount = statement.getParameterMetaData().getParameterCount();
            statement.setInt(paramCount - 1, pageable.getPageSize());
            statement.setInt(paramCount, (pageable.getPageNumber() - 1) * pageable.getPageSize());

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

    default <RE> Page<RE> findAllWhere(MappableCustomQuery<ID, E, RE> customQuery, Pageable pageable) {
        List<RE> entities = new ArrayList<>();

        try {
            Connection connection = databaseConnection.getConnection();
            PreparedStatement statement =
                    connection.prepareStatement(customQuery.getQueryString() + " limit ? offset ?");

            customQuery.fillStatement(statement);

            int paramCount = statement.getParameterMetaData().getParameterCount();
            statement.setInt(paramCount - 1, pageable.getPageSize());
            statement.setInt(paramCount, (pageable.getPageNumber() - 1) * pageable.getPageSize());

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                RE mappedEntity = customQuery.map(createEntityFrom(resultSet));
                entities.add(mappedEntity);
            }
            return new PageImplementation<>(pageable, entities.stream());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
