package com.example.sem7gui.repository.db;

import com.example.sem7gui.domain.User;
import com.example.sem7gui.repository.paging.Page;
import com.example.sem7gui.repository.paging.PageImplementation;
import com.example.sem7gui.repository.paging.Pageable;
import com.example.sem7gui.repository.paging.PagingRepository;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class UserDBPagingRepository extends UserDBRepository  implements PagingRepository<Long, User>
{
    public UserDBPagingRepository(String url, String username, String password) {
        super(url, username, password);
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
//        Stream<User> result = StreamSupport.stream(this.findAll().spliterator(), false)
//                .skip((pageable.getPageNumber() - 1)  * pageable.getPageSize())
//                .limit(pageable.getPageSize());
//        return new PageImplementation<>(pageable, result);

        Set<User> users = new HashSet<>();

        try (Connection connection = DriverManager.getConnection(url, username, password);
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
