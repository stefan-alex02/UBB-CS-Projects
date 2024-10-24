package ir.map.g221.guisocialnetwork.persistence.inmemoryrepos;

import ir.map.g221.guisocialnetwork.domain.entities.Entity;
import ir.map.g221.guisocialnetwork.domain.validation.ArgumentValidator;
import ir.map.g221.guisocialnetwork.domain.validation.Validator;
import ir.map.g221.guisocialnetwork.exceptions.ValidationException;
import ir.map.g221.guisocialnetwork.persistence.Repository;
import ir.map.g221.guisocialnetwork.persistence.paging.PagingRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryRepository<ID, E extends Entity<ID>> implements Repository<ID, E>, PagingRepository<ID, E> {
    protected final Map<ID, E> entities;
    protected final Validator<E> validator;
    private static final ArgumentValidator argumentValidator = ArgumentValidator.getInstance();

    public InMemoryRepository(Validator<E> validator) {
        this.validator = validator;
        entities = new HashMap<>();
    }

    @Override
    public Optional<E> save(E entity) throws IllegalArgumentException, ValidationException {
        checkForNullEntity(entity);
        validator.validate(entity);
        return Optional.ofNullable(entities.putIfAbsent(entity.getId(), entity));
    }

    @Override
    public E createEntityFrom(ResultSet resultSet) throws SQLException {
        return null;
    }

    @Override
    public String getTableName() {
        return null;
    }

    @Override
    public Optional<E> findOne(ID id) throws IllegalArgumentException {
        checkForNullId(id);
        return Optional.ofNullable(entities.get(id));
    }

    @Override
    public Collection<E> findAll() {
        return entities.values();
    }

    @Override
    public Integer getSize() {
        return entities.size();
    }

    @Override
    public Optional<E> update(E entity) throws IllegalArgumentException, ValidationException {
        checkForNullEntity(entity);
        validator.validate(entity);
        return entities.computeIfPresent(entity.getId(), (key, oldEntity) -> entity) != null ?
                Optional.empty() : Optional.of(entity);
    }

    @Override
    public Optional<E> delete(ID id) throws IllegalArgumentException {
        checkForNullId(id);
        return Optional.ofNullable(entities.remove(id));
    }

    protected static <ID, E extends Entity<ID>> void checkForNullEntity(E entity) {
        try {
            argumentValidator.validate(entity);
        }
        catch(IllegalArgumentException e) {
            throw new IllegalArgumentException("Entity must not be null");
        }
    }

    protected static <ID> void checkForNullId(ID id) throws IllegalArgumentException{
        try {
            argumentValidator.validate(id);
        }
        catch(IllegalArgumentException e) {
            throw new IllegalArgumentException("Id must not be null");
        }
    }
}
