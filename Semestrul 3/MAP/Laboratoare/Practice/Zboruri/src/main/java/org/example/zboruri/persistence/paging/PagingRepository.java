package org.example.zboruri.persistence.paging;

import org.example.zboruri.domain.Entity;
import org.example.zboruri.persistence.DatabaseConnection;
import org.example.zboruri.persistence.Repository;

public interface PagingRepository<ID, E extends Entity<ID>, T1, T2> extends Repository<ID, E> {
    DatabaseConnection databaseConnection = DatabaseConnection.getSingleInstance();

    Page<E> findAllWhere(Pageable pageable, T1 havingValue1, T2 havingValue2, T2 havingValue3);
}
