package ir.map.g221.persistence.inmemoryrepos;

import ir.map.g221.domain.entities.Entity;
import ir.map.g221.domain.validation.ArgumentValidator;
import ir.map.g221.domain.validation.Validator;
import ir.map.g221.exceptions.ValidationException;
import ir.map.g221.persistence.Repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryRepository<ID, E extends Entity<ID>> implements Repository<ID, E> {
    private final Map<ID, E> entities;
    private final Validator<E> validator;
    private static final ArgumentValidator argumentValidator = ArgumentValidator.getInstance();

    public InMemoryRepository(Validator<E> validator) {
        this.validator = validator;
        entities = new HashMap<>();
    }

    @Override
    public E add(E entity) throws IllegalArgumentException, ValidationException {
        if (entity == null) {
            throw new IllegalArgumentException("Entity must not be null.");
        }
        validator.validate(entity);
        if (findOne(entity.getId()) != null) {
            return null;
        }
        entities.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public Optional<E> findOne(ID id) throws IllegalArgumentException {
        checkForNullId(id);
        return Optional.ofNullable(entities.get(id));
    }

    public Collection<E> findAll() {
        return entities.values();
    }

    @Override
    public int getSize() {
        return entities.size();
    }

    @Override
    public E update(E entity) throws IllegalArgumentException, ValidationException {
        if (entity == null) {
            throw new IllegalArgumentException("Entity must not be null.");
        }
        validator.validate(entity);
        if (findOne(entity.getId()) != null) {
            return null;
        }
        entities.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public E delete(ID id) throws IllegalArgumentException {
        checkForNullId(id);
        E entity = findOne(id);

        if (entity == null) {
            return null;
        }
        entities.remove(id);
        return entity;
    }

    private static <ID> void checkForNullId(ID id) throws IllegalArgumentException{
        try {
            argumentValidator.validate(id);
        }
        catch(IllegalArgumentException e) {
            throw new IllegalArgumentException("Id must not be null");
        }
    }
}
