package org.example.repository.dbRepository;

import org.example.domain.Regizor;
import org.example.repository.Repository;

import java.sql.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class RegizorDBRepository implements Repository<Long, Regizor> {
    private String url;
    private String username;
    private String password;

    public RegizorDBRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public Optional<Regizor> findOne(Long aLong) {
        return Optional.empty();
    }

    @Override
    public Iterable<Regizor> findAll() {
        Set<Regizor> regizori = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(
                     "select * from regizori");
             ResultSet result = statement.executeQuery();
             ) {
            while(result.next()) {
                Long id = result.getLong("id");
                String nume = result.getString("nume");
                Regizor regizor = new Regizor(nume);
                regizor.setId(id);
                regizori.add(regizor);
            }
            return regizori;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Regizor> save(Regizor entity) {
        return Optional.empty();
    }

    @Override
    public Optional<Regizor> delete(Long aLong) {
        return Optional.empty();
    }

    @Override
    public Optional<Regizor> update(Regizor entity) {
        return Optional.empty();
    }
}
