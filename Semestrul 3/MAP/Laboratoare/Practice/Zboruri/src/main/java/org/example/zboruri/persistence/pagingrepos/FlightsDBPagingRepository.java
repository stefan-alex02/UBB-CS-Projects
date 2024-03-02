package org.example.zboruri.persistence.pagingrepos;

import org.example.zboruri.domain.Flight;
import org.example.zboruri.persistence.DatabaseConnection;
import org.example.zboruri.persistence.dbrepos.FlightDBRepository;
import org.example.zboruri.persistence.paging.Page;
import org.example.zboruri.persistence.paging.PageImplementation;
import org.example.zboruri.persistence.paging.Pageable;
import org.example.zboruri.persistence.paging.PagingRepository;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class FlightsDBPagingRepository extends FlightDBRepository implements PagingRepository<Long, Flight, LocalDate, String> {
    public FlightsDBPagingRepository(DatabaseConnection databaseConnection) {
        super(databaseConnection);
    }

    @Override
    public Page<Flight> findAllWhere(Pageable pageable, LocalDate havingValue1, String havingValue2, String havingValue3) {
        Set<Flight> entities = new HashSet<>();

        try {
            Connection connection = databaseConnection.getConnection();
            PreparedStatement statement =
                    connection.prepareStatement("""
                            select * from flights F
                            where DATE(F."departureTime") = DATE(?) and
                            F."from" = ? and F."to" = ?
                            limit ? offset ?""");
            statement.setTimestamp(1, Timestamp.valueOf(havingValue1.atStartOfDay()));
            statement.setString(2, havingValue2);
            statement.setString(3, havingValue3);
            statement.setInt(4, pageable.getPageSize());
            statement.setInt(5, (pageable.getPageNumber() - 1) * pageable.getPageSize());

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Flight flight = getFlight(resultSet);
                entities.add(flight);
            }
            return new PageImplementation<>(pageable, entities.stream());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
