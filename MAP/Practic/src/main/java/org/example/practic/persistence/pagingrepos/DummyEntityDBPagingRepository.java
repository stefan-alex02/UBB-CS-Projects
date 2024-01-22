package org.example.practic.persistence.pagingrepos;

import org.example.practic.domain.DummyEntity;
import org.example.practic.persistence.DatabaseConnection;
import org.example.practic.persistence.dbrepos.DummyEntityDBRepository;
import org.example.practic.persistence.paging.Page;
import org.example.practic.persistence.paging.PageImplementation;
import org.example.practic.persistence.paging.Pageable;
import org.example.practic.persistence.paging.PagingRepository;
import org.example.practic.validation.Validator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class DummyEntityDBPagingRepository extends DummyEntityDBRepository implements PagingRepository<Long, DummyEntity> {
    public DummyEntityDBPagingRepository(DatabaseConnection databaseConnection,
                                         Validator<DummyEntity> validator) {
        super(databaseConnection, validator);
    }

    @Override
    public Page<DummyEntity> findAll(Pageable pageable) {
        Set<DummyEntity> entities = new HashSet<>();

        try {
            Connection connection = databaseConnection.getConnection();
            PreparedStatement statement =
                    connection.prepareStatement("""
                            select * from dummies D
                            limit ? offset ?""");
//            statement.setLong(1, havingValue);
            statement.setInt(1, pageable.getPageSize());
            statement.setInt(2, (pageable.getPageNumber() - 1) * pageable.getPageSize());

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                DummyEntity entity = getEntity(resultSet);
                entities.add(entity);
            }
            return new PageImplementation<>(pageable, entities.stream());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
