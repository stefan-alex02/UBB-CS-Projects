package org.example.practic.persistence.paging;

import org.example.practic.domain.Entity;
import org.example.practic.persistence.DatabaseConnection;
import org.example.practic.persistence.Repository;

public interface PagingRepository<ID, E extends Entity<ID>> extends Repository<ID, E> {
    DatabaseConnection databaseConnection = DatabaseConnection.getSingleInstance();

    Page<E> findAll(Pageable pageable);
}
