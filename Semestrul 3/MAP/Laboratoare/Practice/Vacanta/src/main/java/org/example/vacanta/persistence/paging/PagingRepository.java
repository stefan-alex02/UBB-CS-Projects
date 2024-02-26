package org.example.vacanta.persistence.paging;

import org.example.modelpractic.domain.Entity;
import org.example.modelpractic.persistence.DatabaseConnection;
import org.example.modelpractic.persistence.Repository;

public interface PagingRepository<ID, E extends Entity<ID>, T> extends Repository<ID, E> {
    DatabaseConnection databaseConnection = DatabaseConnection.getSingleInstance();

    Page<E> findAllWhere(Pageable pageable, T havingValue);
}
